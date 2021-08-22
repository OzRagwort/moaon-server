create table channels (
channels_idx bigint not null auto_increment,
categories_idx bigint,
channels_id varchar(255),
channels_name varchar(255),
channels_thumbnail varchar(255),
channels_subscribers INT default 0,
channels_uploads_list varchar(255),
channels_banner_external_url varchar(255),
created_date datetime(6),
modified_date datetime(6),
primary key (channels_idx)
) engine=InnoDB;