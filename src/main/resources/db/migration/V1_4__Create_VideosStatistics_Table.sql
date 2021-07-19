create table videos_statistics (
videos_statistics_idx bigint not null auto_increment,
videos_statistics_comment_count INT default 0,
videos_statistics_dislike_count INT default 0,
videos_statistics_like_count INT default 0,
videos_statistics_view_count INT default 0,
videos_statistics_score double precision,
videos_snippet_idx bigint,
primary key (videos_statistics_idx)
) engine=InnoDB;