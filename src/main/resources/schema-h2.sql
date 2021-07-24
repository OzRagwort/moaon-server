DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES CASCADE;
DROP TABLE IF EXISTS SPRING_SESSION CASCADE;
DROP TABLE IF EXISTS videos_tags CASCADE;
DROP TABLE IF EXISTS videos_statistics CASCADE;
DROP TABLE IF EXISTS videos_snippet CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BLOB NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;


-- create table categories
create table categories (
categories_idx bigint not null auto_increment,
categories_name varchar(255),
primary key (categories_idx)
) engine=InnoDB;

-- create table channels
create table channels (
channels_idx bigint not null auto_increment,
channels_id varchar(255),
channels_name varchar(255),
channels_thumbnail varchar(255),
channels_subscribers INT default 0,
channels_uploads_list varchar(255),
channels_banner_external_url varchar(255),
created_date datetime(6),
modified_date datetime(6),
categories_idx bigint,
primary key (channels_idx)
) engine=InnoDB;

-- create table videos_snippet
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

-- create table videos_statistics
create table videos_statistics (
videos_statistics_idx bigint not null auto_increment,
videos_snippet_idx bigint,
videos_statistics_comment_count INT default 0,
videos_statistics_dislike_count INT default 0,
videos_statistics_like_count INT default 0,
videos_statistics_view_count INT default 0,
videos_statistics_score double precision,
primary key (videos_statistics_idx)
) engine=InnoDB;

-- create table videos_tags
create table videos_tags (
videos_snippet_idx bigint not null,
videos_tags_tags varchar(255)
) engine=InnoDB;

-- alter table add unique key
alter table channels add constraint UK_3li78kasrcje8typ7y0aapllg unique (channels_id);
alter table videos_snippet add constraint UK_6iorbpe7xk27mjsj7r66ybcd9 unique (videos_snippet_id);
alter table videos_tags add constraint UKniaky705rcvep7jjcm04j9led unique (videos_snippet_idx, videos_tags_tags);

-- alter table add foreign key
alter table channels add constraint FKjxpru3n3vf6727lbtj757ocyi foreign key (categories_idx) references categories (categories_idx);
alter table videos_snippet add constraint FKenmvo5x09qwxsd8vuerspug8x foreign key (channels_idx) references channels (channels_idx);
alter table videos_statistics add constraint FKpfgyp78lql276ibppytpeab99 foreign key (videos_snippet_idx) references videos_snippet (videos_snippet_idx);
alter table videos_tags add constraint FKccn51mw14ejj3x4wspdhykagn foreign key (videos_snippet_idx) references videos_snippet (videos_snippet_idx);
