create table comment
(
	id int auto_increment,
	parent_id bigint not null,
	type int not null,
	commentator int,
	gmt_create bigint,
	gmt_modified bigint,
	like_count bigint default 0,
	constraint comment_pk
		primary key (id)
);