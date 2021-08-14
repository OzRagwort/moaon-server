alter table categories default character set utf8mb4 collate utf8mb4_bin;

alter table channels default character set utf8mb4 collate utf8mb4_bin;

alter table videos default character set utf8mb4 collate utf8mb4_bin;

alter table videos_tags default character set utf8mb4 collate utf8mb4_bin;

alter table channels modify channels_name varchar(255) collate utf8mb4_bin;

alter table videos modify videos_name varchar(255) collate utf8mb4_bin;

alter table videos modify videos_description TEXT collate utf8mb4_bin;

alter table videos_tags modify videos_tags_tags varchar(255) collate utf8mb4_bin;
