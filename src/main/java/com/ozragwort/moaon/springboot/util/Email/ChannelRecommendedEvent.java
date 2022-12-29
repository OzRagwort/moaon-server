package com.ozragwort.moaon.springboot.util.Email;

public class ChannelRecommendedEvent {

    private final String content;

    public ChannelRecommendedEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
