package com.ozragwort.moaon.springboot.util.Email;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailEventHandler {

    private static final String ADMIN_EMAIL = "ozragwort@gmail.com";

    private final EmailUtilImpl emailUtil;

    public EmailEventHandler(EmailUtilImpl emailUtil) {
        this.emailUtil = emailUtil;
    }

    @EventListener
    public void sendChannelRecommendEmail(ChannelRecommendedEvent event) {
        emailUtil.sendEmail(
            ADMIN_EMAIL,
            "애니멀봄 에서 채널 추천",
            event.getContent()
        );
    }
}
