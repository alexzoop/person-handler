use persons_db;

drop table if exists persons;
create table persons (
	id serial primary key,
	name varchar(255),
	birthday date,
	personal_account long unsigned
) comment = 'Person';

drop table if exists hobbies;
create table hobbies (
	id serial primary key,
	hobby_name varchar(255),
	complexity int unsigned,
	person_id int unsigned
) comment = 'Hobby'