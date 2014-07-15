create table ng_albums 
			(ng_albums_id int not null auto_increment primary key, 
			ng_singers_id int, 
			album_name varchar(100), 
			release_year varchar(4), 
			ng_record_id int, 
			unique (album_name, release_year), 
			foreign key (ng_singers_id) references ng_singers(ng_singers_id), 
			foreign key (ng_record_id) references ng_record_co(ng_record_id));