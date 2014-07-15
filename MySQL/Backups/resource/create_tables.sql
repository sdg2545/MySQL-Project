create table ng_users (ng_users_id int not null auto_increment primary key, username varchar(100), password varchar(100), unique (username));
create table ng_singers (ng_singers_id int not null auto_increment primary key, name varchar(100), dob date, sex varchar(10), unique (name));
create table ng_albums (ng_albums_id int not null auto_increment primary key, ng_singers_id int, album_name varchar(100), release_year varchar(4), record_company varchar(200), unique (album_name, release_year, record_company), foreign key (ng_singers_id) references ng_singers(ng_singers_id));
