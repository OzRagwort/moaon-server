package com.ozragwort.moaon.springboot.youtube.YoutubeApiSerializer;

import com.google.api.services.youtube.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/*
part :  O   id
        O   snippet
        O   contentDetails
        O   statistics
        X   status
        X   auditDetails
        O   brandingSettings (just image.bannerExternalUrl)
        X   contentOwnerDetails
        X   localizations
        X   topicDetails
 */
@Component
public class ChannelDeserializer {

    public ChannelListResponse deserialize(JSONObject jsonObject) {
        ChannelListResponse channelListResponse = new ChannelListResponse();

        channelListResponse.setKind((String) jsonObject.get("kind"));
        channelListResponse.setEtag((String) jsonObject.get("etag"));
        channelListResponse.setItems(buildItems((JSONArray) jsonObject.get("items")));
        channelListResponse.setPageInfo(buildPageInfo((JSONObject) jsonObject.get("pageInfo")));
        channelListResponse.setNextPageToken((String) jsonObject.get("nextPageToken"));
        channelListResponse.setPrevPageToken((String) jsonObject.get("prevPageToken"));

        return channelListResponse;
    }

    private PageInfo buildPageInfo(JSONObject jsonObject) {
        PageInfo pageInfo = new PageInfo();

        pageInfo.setTotalResults(((Long) jsonObject.get("totalResults")).intValue());
        pageInfo.setResultsPerPage(((Long) jsonObject.get("resultsPerPage")).intValue());

        return pageInfo;
    }

    private List<Channel> buildItems(JSONArray items) {
        List<Channel> channelList = new ArrayList<>();
        Channel channel;

        for (int i = 0 ; i < items.size() ; i++) {
            channel = new Channel();

            JSONObject ob = (JSONObject) items.get(i);

            try {
                channel.setKind((String) ob.get("kind"));
                channel.setEtag((String) ob.get("etag"));
                channel.setId((String) ob.get("id"));
                channel.setSnippet(buildSnippet((JSONObject) ob.get("snippet")));
                channel.setContentDetails(buildContentDetails((JSONObject) ob.get("contentDetails")));
                channel.setStatistics(buildStatistic((JSONObject) ob.get("statistics")));
                channel.setBrandingSettings(buildBrandingSettings((JSONObject) ob.get("brandingSettings")));
            } catch (NullPointerException e) {
                System.out.printf("Channel Deserializer build items [%s]%n", ob.get("id"));
                e.printStackTrace();
            }

            channelList.add(channel);
        }

        return channelList;
    }

    private ChannelBrandingSettings buildBrandingSettings(JSONObject brandingSettings) {
        ChannelBrandingSettings channelBrandingSettings = new ChannelBrandingSettings();

        try {
            channelBrandingSettings.setImage(buildImageSettings((JSONObject) brandingSettings.get("image")));
        } catch (NullPointerException e) {
            channelBrandingSettings.setImage(null);
        }

        return channelBrandingSettings;
    }

    private ImageSettings buildImageSettings(JSONObject image) {
        ImageSettings imageSettings = new ImageSettings();

        imageSettings.setBannerExternalUrl((String) image.get("bannerExternalUrl"));

        return imageSettings;
    }

    private ChannelStatistics buildStatistic(JSONObject statistic) {
        ChannelStatistics channelStatistics = new ChannelStatistics();

        channelStatistics.setViewCount(new BigInteger((String) statistic.get("viewCount")));
        channelStatistics.setVideoCount(new BigInteger((String) statistic.get("videoCount")));
        if (statistic.containsKey("subscriberCount")) {
            channelStatistics.setSubscriberCount(new BigInteger((String) statistic.get("subscriberCount")));
        } else {
            channelStatistics.setSubscriberCount(BigInteger.ZERO);
        }

        return channelStatistics;
    }

    private ChannelContentDetails buildContentDetails(JSONObject contentDetails) {
        ChannelContentDetails channelContentDetails = new ChannelContentDetails();

        channelContentDetails.setRelatedPlaylists(buildRelatedPlaylists((JSONObject) contentDetails.get("relatedPlaylists")));

        return channelContentDetails;
    }

    private ChannelContentDetails.RelatedPlaylists buildRelatedPlaylists(JSONObject relatedPlaylists) {
        ChannelContentDetails.RelatedPlaylists playlists = new ChannelContentDetails.RelatedPlaylists();

        playlists.setUploads((String) relatedPlaylists.get("uploads"));
        playlists.setLikes((String) relatedPlaylists.get("likes"));
        playlists.setFavorites((String) relatedPlaylists.get("favorites"));

        return playlists;
    }

    private ChannelSnippet buildSnippet(JSONObject snippet) {
        ChannelSnippet channelSnippet = new ChannelSnippet();

        channelSnippet.setTitle((String) snippet.get("title"));
        channelSnippet.setDescription((String) snippet.get("description"));
        channelSnippet.setCustomUrl((String) snippet.get("customUrl"));
        channelSnippet.setPublishedAt((String) snippet.get("publishedAt"));
        channelSnippet.setThumbnails(buildThumbnailDetails((JSONObject) snippet.get("thumbnails")));
        channelSnippet.setDefaultLanguage((String) snippet.get("defaultLanguage"));
        channelSnippet.setLocalized(buildLocalized((JSONObject) snippet.get("localized")));
        channelSnippet.setCountry((String) snippet.get("country"));

        return channelSnippet;
    }

    private ChannelLocalization buildLocalized(JSONObject localized) {
        ChannelLocalization channelLocalization = new ChannelLocalization();

        channelLocalization.setTitle((String) localized.get("title"));
        channelLocalization.setDescription((String) localized.get("description"));

        return channelLocalization;
    }

    private ThumbnailDetails buildThumbnailDetails(JSONObject thumbnails) {
        ThumbnailDetails thumbnailDetails = new ThumbnailDetails();

        try {
            thumbnailDetails.setDefault(buildThumbnail((JSONObject) thumbnails.get("default")));
            thumbnailDetails.setMedium(buildThumbnail((JSONObject) thumbnails.get("medium")));
            thumbnailDetails.setHigh(buildThumbnail((JSONObject) thumbnails.get("high")));
            thumbnailDetails.setStandard(buildThumbnail((JSONObject) thumbnails.get("standard")));
            thumbnailDetails.setMaxres(buildThumbnail((JSONObject) thumbnails.get("maxres")));
        } catch (NullPointerException e) {
            e.getMessage();
        }

        return thumbnailDetails;
    }

    private Thumbnail buildThumbnail(JSONObject thumbnails) {
        Thumbnail thumbnail = new Thumbnail();

        try {
            thumbnail.setUrl((String) thumbnails.get("url"));
            thumbnail.setWidth((Long) thumbnails.get("width"));
            thumbnail.setHeight((Long) thumbnails.get("height"));
        } catch (NullPointerException e) {
            return null;
        }

        return thumbnail;
    }

}
