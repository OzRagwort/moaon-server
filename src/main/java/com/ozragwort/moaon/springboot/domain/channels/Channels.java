package com.ozragwort.moaon.springboot.domain.channels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

//    @JsonManagedReference
//    @OneToMany(mappedBy = "channels", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Videos.class)
//    private List<Videos> videos = new ArrayList<>();

    @Column(name = "channel_id", unique = true)
    private String channelId;

    private String channelName; // part.snippet.title

    private String channelThumbnail; // part.snippet.thumbnails

    private String uploadsList; // part.contentDetails.uploads

    @Column(columnDefinition = "INT default 0")
    private int subscribers;

    @Builder
    public Channels(Categories categories, String channelId, String channelName, String channelThumbnail, String uploadsList, int subscribers) {
        this.categories = categories;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
    }

    public void update(String channelName, String channelThumbnail, String uploadsList, int subscribers) {
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
    }

}
