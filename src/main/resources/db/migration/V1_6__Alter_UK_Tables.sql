alter table channels add constraint UK_3li78kasrcje8typ7y0aapllg unique (channels_id);

alter table videos add constraint UK_6iorbpe7xk27mjsj7r66ybcd9 unique (videos_id);

alter table videos_tags add constraint UKniaky705rcvep7jjcm04j9led unique (videos_idx, videos_tags_tags);
