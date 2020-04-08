drop table if exists page;
create table page
(
	id int,
	title varchar(50),
	createTime varchar default 30,
	draft bit,
	type varchar(20),
	layout varchar(30),
	url varchar(255),
	permLink varchar(255),
	description text,
	content text,
	toc text,
	constraint page_pk
		primary key (id)
);
drop table if exists tag;
create table tag
(
	id int,
	name varchar(20),
	constraint tag_pk
		primary key (id)
);
drop table if exists category;
create table category
(
	id int,
	name varchar(20),
	constraint category_pk
		primary key (id)
);
drop table if exists page_tag;
create table page_tag
(
	pageId int,
	tagId int
);
drop table if exists page_category;
create table page_category
(
	pageId int,
	categoryId int
);


