-- insert categories data
insert into categories(categories_idx, categories_name)
values (null, 'pet & aniaml');

-- insert channels data
insert into channels(
                        channels_idx,
                        categories_idx,
                        channels_id,
                        channels_name,
                        channels_thumbnail,
                        channels_uploads_list,
                        channels_subscribers,
                        channels_banner_external_url)
values (null, 1, 'testChannelId1', 'testChannelName1', 'testThumbnail1', 'testUploadsList1', 0, 'testBannerUrl1'),
       (null, 1, 'testChannelId2', 'testChannelName2', 'testThumbnail2', 'testUploadsList2', 0, 'testBannerUrl2'),
       (null, 1, 'testChannelId3', 'testChannelName3', 'testThumbnail3', 'testUploadsList3', 0, 'testBannerUrl3'),
       (null, 1, 'testChannelId4', 'testChannelName4', 'testThumbnail4', 'testUploadsList4', 0, 'testBannerUrl4'),
       (null, 1, 'testChannelId5', 'testChannelName5', 'testThumbnail5', 'testUploadsList5', 0, 'testBannerUrl5'),
       (null, 1, 'testChannelId6', 'testChannelName6', 'testThumbnail6', 'testUploadsList6', 0, 'testBannerUrl6'),
       (null, 1, 'testChannelId7', 'testChannelName7', 'testThumbnail7', 'testUploadsList7', 0, 'testBannerUrl7'),
       (null, 1, 'testChannelId8', 'testChannelName8', 'testThumbnail8', 'testUploadsList8', 0, 'testBannerUrl8'),
       (null, 1, 'testChannelId9', 'testChannelName9', 'testThumbnail9', 'testUploadsList9', 0, 'testBannerUrl9');

-- insert videos data
insert into videos(
                        videos_idx,
                        channels_idx,
                        videos_id,
                        videos_name,
                        videos_thumbnail,
                        videos_description,
                        videos_published_date,
                        videos_duration,
                        videos_view_count,
                        videos_like_count,
                        videos_dislike_count,
                        videos_comment_count,
                        videos_score)
values (null, 1, 'testVideoId1', 'testVideoName1', 'testThumbnail1', 'testDescrption1', '2021-01-01 00:00:00.0', 120, 1, 1, 1, 1, 1),
       (null, 1, 'testVideoId2', 'testVideoName2', 'testThumbnail2', 'testDescrption2', '2021-01-01 00:00:00.0', 240, 2, 2, 2, 2, 2),
       (null, 1, 'testVideoId3', 'testVideoName3', 'testThumbnail3', 'testDescrption3', '2021-01-01 00:00:00.0', 360, 3, 3, 3, 3, 3),
       (null, 2, 'testVideoId4', 'testVideoName4', 'testThumbnail4', 'testDescrption4', '2021-01-01 00:00:00.0', 480, 4, 4, 4, 4, 4);

-- insert videos_tags data
insert into videos_tags(
                        videos_idx,
                        videos_tags_tags)
values (1, 'pet'),
       (1, 'cat'),
       (1, 'animal'),
       (2, 'pet'),
       (2, 'cat'),
       (3, 'pet'),
       (4, 'pet'),
       (4, 'cat'),
       (4, 'animal');
