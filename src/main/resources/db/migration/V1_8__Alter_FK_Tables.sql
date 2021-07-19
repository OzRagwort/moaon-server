alter table channels add constraint FKjxpru3n3vf6727lbtj757ocyi foreign key (categories_idx) references categories (categories_idx);

alter table videos_snippet add constraint FKenmvo5x09qwxsd8vuerspug8x foreign key (channels_idx) references channels (channels_idx);

alter table videos_statistics add constraint FKpfgyp78lql276ibppytpeab99 foreign key (videos_snippet_idx) references videos_snippet (videos_snippet_idx);

alter table videos_tags add constraint FKccn51mw14ejj3x4wspdhykagn foreign key (videos_snippet_idx) references videos_snippet (videos_snippet_idx);
