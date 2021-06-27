package com.ozragwort.moaon.springboot.v1.component;

import org.springframework.stereotype.Component;

@Component
public class CheckIdType {

    public boolean checkChannelId(String channelId) {
        return channelId.length() == 24 && channelId.startsWith("UC");
    }

    public boolean checkVideoId(String videoId) {
        return videoId.length() == 11;
    }

}
