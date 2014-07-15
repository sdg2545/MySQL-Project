create table ng_users 
			(ng_users_id int not null auto_increment primary key, 
			username varchar(100), 
			password varchar(100), 
			unique (username));
create table ng_singers 
			(ng_singers_id int not null auto_increment primary key, 
			name varchar(100), 
			dob date, 
			sex varchar(10), 
			unique (name));
create table ng_albums 
			(ng_albums_id int not null auto_increment primary key, 
			ng_singers_id int, 
			album_name varchar(100), 
			release_year varchar(4), 
			ng_record_id int, 
			unique (album_name, release_year), 
			foreign key (ng_singers_id) references ng_singers(ng_singers_id), 
			foreign key (ng_record_id) references ng_record_co(ng_record_id));
create table ng_record_co 
			(ng_record_id int not null auto_increment primary key, 
			record_company varchar(200) not null);
create table ng_admins 
			(ng_admin_id int not null primary key,
			foreign key (ng_admin_id) references ng_users(ng_users_id));