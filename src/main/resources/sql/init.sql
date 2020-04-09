drop table if exists page;
create table page
(
	id int auto_increment,
	title varchar(50),
	date varchar default 30,
	draft bit,
	type varchar(20),
	layout varchar(30),
	url varchar(255),
	perm_link varchar(255),
	description varchar(255),
	content clob,
	constraint page_pk
		primary key (id)
);
create unique index page_url_1_uindex
	on page (perm_link);
drop table if exists tag;
create table tag
(
	id int auto_increment,
	name varchar(20),
	constraint tag_pk
		primary key (id)
);
drop table if exists category;
create table category
(
	id int auto_increment,
	name varchar(20),
	constraint category_pk
		primary key (id)
);
drop table if exists page_tag;
create table page_tag
(
	page_id int,
	tag_id int
);
drop table if exists page_category;
create table page_category
(
	page_id int,
	category_id int
);
drop table if exists static_resource;
create table static_resource(
    uri varchar(255),
    file_path varchar(255)
);


