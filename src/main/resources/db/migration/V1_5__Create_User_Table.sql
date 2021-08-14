create table user (
id bigint not null auto_increment,
name varchar(255) not null,
email varchar(255) not null,
picture varchar(255),
role varchar(255) not null,
primary key (id)
) engine=InnoDB;