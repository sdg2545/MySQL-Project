/**
 * @author Sergio Gonzalez II
 * NextGate data loader class
 */

//imports:
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
//Class to load default data to database ng_music
public class ClsDataLoaderNgMusic {
    //Path to where the data text files are stored
    private final String _PATH = "C:\\Users\\Sergio Gonzalez II\\Documents\\Job Files\\Job\\NextGate\\TakeHomeProject\\resource\\";
    private String[] _fileList;
    private java.sql.Connection con = null;
    //Connection string variables:
    private final String url ="jdbc:mysql://";
    private final String serverName= "localhost";
    private final String portNumber = "3306";
    private final String databaseName= "ng_music";
    private final String userName = "ng_user";
    private final String password = "ng";     
    
    //Class constructor: takes in an array of files
    public ClsDataLoaderNgMusic(String[] fileList, int fileListSize)
    {
        //Checkes for correct parameters
        if(fileList.length == 0 || fileListSize == 0)
            System.out.println("Incorrect parameters.");
        //Copies parameter array to class variable
        this._fileList = new String[fileListSize];        
        System.arraycopy(fileList, 0, this._fileList, 0, fileListSize);
    }
    
    //Method to load data from each files passed into the class
    public void loadData()
    {
        //Loads connection to the class variable
        getConnection();
        String table = "";
        String fileName = "";
        String[] columnList;
        //Loop to get the column names and table to insert data into for each file
        for (int i = 0; i < _fileList.length; i++) 
        {
            fileName = _fileList[i].toString(); 
            table = getTableFromFile(fileName);              
            columnList = getColumnList(fileName); 
            if (columnList != null) importData(fileName,table,columnList);            
        }
        //Closes connection of the class variable
        closeConnection(); 
    }
    
    //Method to import data takes in file to import from, table to import to, 
    //  and column names of the table
    private void importData(String fileName, String tableName, String[] columnList)
    {        
        Statement stmt;
        String query = "";        
        try
        {
            System.out.println("Importing to: " + tableName);
            stmt = con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);             
            //Load query to import data
            query = buildLoadSQL(fileName,tableName,columnList);
            stmt.executeUpdate(query.replace("\\", "\\\\"));             
            //Load query to format data columns
            query = buildUpdateSQL(fileName,tableName,columnList);
            stmt.executeUpdate(query);
            System.out.println("Import successful!");
        }
        catch(Exception ex)
        {
            System.out.println("Error while importing data...");
            System.out.println(ex.toString());
            stmt = null;
        }
    }
    
    //Method to build the SQL statement, SQL loads entire file 
    private String buildLoadSQL(String fileName, String tableName, String[] columnList)
    {
       //columnPortion creates the ending column portion of the SQL cmd
       String columnPortion = "(";
       for(String item : columnList)
           columnPortion += item + ", ";
       //Removes last ", "
       columnPortion = columnPortion.substring(0, columnPortion.length() - 2);
       columnPortion += ");";       
       String query = "LOAD DATA LOCAL INFILE '" + _PATH +  fileName +
                    "' INTO TABLE " + tableName + 
                    " FIELDS TERMINATED By '|'" + 
                    " LINES TERMINATED BY '\r\n'" +
                    " IGNORE 1 LINES " + columnPortion;
        return query;
    }
    
    //Method to build the SQL statement, SQL loads entire file 
    private String buildUpdateSQL(String fileName, String tableName, String[] columnList)
    {
        //trimportion strings removes extra spaces 
        String trimPortion1 = " = TRIM(Replace(Replace(Replace(";
        String trimPortion2 = ",'\\t',''),'\\n',''),'\\r',''))";
        String query = "UPDATE " + tableName + " set ";
        for(String item : columnList) query += item +trimPortion1 + item + trimPortion2 + ",";
        query = query.substring(0, query.length() - 1);
        query += ";";        
        return query;
    }
    
    //Sets up connection to the MySQL database
    private void getConnection()
    {
        try
        {               
           Class.forName ("com.mysql.jdbc.Driver").newInstance();  
           System.out.println("Connecting to " + getConnectionUrl());               
           con = java.sql.DriverManager.getConnection(getConnectionUrl(),userName,password);
           if(con!=null) System.out.println("Connection Successful!");
        }catch(Exception ex){
           ex.printStackTrace();
           System.out.println("Error Trace in getConnection() : ");
        }
    }
    
    //Method: returns connection string
    private String getConnectionUrl()
    {
          return url+serverName+"/"+databaseName;
    }
    
    //Method to close the connection
    private void closeConnection()
    {
        try
        {
           if(con!=null) con.close();
           con=null;
        }catch(Exception ex)
        {
           ex.printStackTrace();
        }
     }
    
    //Method reads in and returns list of columns from the text file
    private String[] getColumnList(String fileName)
    {
        String[] columnList;
        String firstLine = "";
        Scanner scFileReader;
        scFileReader = openFile(fileName);
        if(scFileReader == null) return null;
        firstLine = scFileReader.nextLine();
        //splits by | delimiter as in the file
        columnList = firstLine.split("\\|");
        for(int i = 0; i < columnList.length; i++) 
            columnList[i] = columnList[i].trim();
        return columnList;
    }
    
    //Method to open file within a scanner
    private Scanner openFile(String fileName)
    {
        try 
        {
            System.out.println("Opening file: " + fileName);
            return new Scanner(new File(_PATH + fileName));
        } catch (FileNotFoundException ex) {
            System.out.println("Could not open file!");
            ex.printStackTrace();
            return null;
        }
    }

    //Gets hardcoded tables from parameter
    private String getTableFromFile(String fileName) 
    {
        String returnValue = "";
        if(fileName == null)returnValue = null;
        else if(fileName.contains("ng_users")) returnValue = "ng_users";
        else if(fileName.contains("ng_singers")) returnValue = "ng_singers";
        else if(fileName.contains("ng_albums")) returnValue = "ng_albums";
        else if(fileName.contains("ng_record")) returnValue = "ng_record_co";
        return returnValue;
    }
}