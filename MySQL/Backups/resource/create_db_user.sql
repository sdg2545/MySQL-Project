create database ng_music;
create user 'ng_user'@'%' identified by 'ng';
grant all privileges on ng_music.* to ng_user with grant option;
commit;
quit;
