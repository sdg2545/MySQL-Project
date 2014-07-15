/**
 *
 * @author Sergio Gonzalez II
 * NextGate main method to run data loader class
 */
public class Driver {
    public static void main(String [] args)
    {
        //Pass in cmd-line arguments to the data loader
        ClsDataLoaderNgMusic dataLoader = new ClsDataLoaderNgMusic(args, args.length);        
        dataLoader.loadData();
    }
}
