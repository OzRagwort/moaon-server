package com.ozragwort.moaon.springboot.domain.videos;

import org.apache.tomcat.jni.Time;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideosRepositoryTest {

    @Autowired
    VideosRepository videosRepository;

    @After
    public void cleanup() {
        videosRepository.deleteAll();
    }

    @Test
    public void VideosRepository_테스트() {
        //given
        Videos channelId = null;
        String videoId = null;
        String videoName = null;
        String videoThumbnail = null;
        String videoDescription = null;
        SimpleDateFormat videoPublishedDate = null;
        Time videoDuration = null;
        boolean videoPublicStatsViewable = false;

        //when

        //then

    }
}
