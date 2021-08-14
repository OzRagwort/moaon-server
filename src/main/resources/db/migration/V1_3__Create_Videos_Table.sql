create table videos (
videos_idx bigint not null auto_increment,
channels_idx bigint,
videos_id varchar(255),
videos_name varchar(255),
videos_thumbnail varchar(255),
videos_description TEXT,
videos_published_date datetime(6),
videos_duration bigint,
videos_comment_count INT default 0,
videos_dislike_count INT default 0,
videos_like_count INT default 0,
videos_view_count INT default 0,
videos_score double precision,
created_date datetime(6),
modified_date datetime(6),
primary key (videos_idx)
) engine=InnoDB;