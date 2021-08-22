package com.ozragwort.moaon.springboot.domain.channels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channels extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channels_idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categories_idx")
    private Categories categories;

    @Column(name = "channels_id", unique = true)
    private String channelId;

    @Column(name = "channels_name")
    private String channelName; // part.snippet.title

    @Column(name = "channels_thumbnail")
    private String channelThumbnail; // part.snippet.thumbnails

    @Column(name = "channels_uploads_list")
    private String uploadsList; // part.contentDetails.uploads

    @Column(name = "channels_subscribers", columnDefinition = "INT default 0")
    private int subscribers;

    @Column(name = "channels_banner_external_url")
    private String bannerExternalUrl; // part.brandingSettings.image.bannerExternalUrl

    @Builder
    public Channels(Categories categories,
                    String channelId,
                    String channelName,
                    String channelThumbnail,
                    String uploadsList,
                    int subscribers,
                    String bannerExternalUrl) {
        this.categories = categories;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
        this.bannerExternalUrl = bannerExternalUrl;
    }

    public void update(String channelName,
                       String channelThumbnail,
                       String uploadsList,
                       int subscribers,
                       String bannerExternalUrl) {
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
        this.bannerExternalUrl = bannerExternalUrl;
    }

}
