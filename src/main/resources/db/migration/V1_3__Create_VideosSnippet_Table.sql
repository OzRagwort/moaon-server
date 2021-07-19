create table videos_snippet (
videos_snippet_idx bigint not null auto_increment,
channels_idx bigint,
videos_snippet_id varchar(255),
videos_snippet_name varchar(255),
videos_snippet_thumbnail varchar(255),
videos_snippet_description TEXT,
videos_snippet_published_date datetime(6),
videos_snippet_duration bigint,
created_date datetime(6),
modified_date datetime(6),
primary key (videos_snippet_idx)
) engine=InnoDB;