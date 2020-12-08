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
        O   status
        X   fileDetails
        X   liveStreamingDetails
        X   localizations
        X   player
        X   processingDetails
        X   recordingDetails
        X   suggestions
        X   topicDetails
 */
@Component
public class VideoDeserializer {

    public VideoListResponse deserialize(JSONObject jsonObject) {
        VideoListResponse videoListResponse = new VideoListResponse();

        videoListResponse.setKind((String) jsonObject.get("kind"));
        videoListResponse.setEtag((String) jsonObject.get("etag"));
        videoListResponse.setItems(buildItems((JSONArray) jsonObject.get("items")));
        videoListResponse.setPageInfo(buildPageInfo((JSONObject) jsonObject.get("pageInfo")));
        videoListResponse.setNextPageToken((String) jsonObject.get("nextPageToken"));
        videoListResponse.setPrevPageToken((String) jsonObject.get("prevPageToken"));

        return videoListResponse;
    }

    private PageInfo buildPageInfo(JSONObject jsonObject) {
        PageInfo pageInfo = new PageInfo();

        pageInfo.setTotalResults(((Long) jsonObject.get("totalResults")).intValue());
        pageInfo.setResultsPerPage(((Long) jsonObject.get("resultsPerPage")).intValue());

        return pageInfo;
    }

    private List<Video> buildItems(JSONArray items) {
        List<Video> videoList = new ArrayList<>();
        Video video;

        for (int i = 0 ; i < items.size() ; i++) {
            video = new Video();

            JSONObject ob = (JSONObject) items.get(i);

            try {
                video.setKind((String) ob.get("kind"));
                video.setEtag((String) ob.get("etag"));
                video.setId((String) ob.get("id"));
                video.setSnippet(buildSnippet((JSONObject) ob.get("snippet")));
                video.setContentDetails(buildContentDetails((JSONObject) ob.get("contentDetails")));
                video.setStatus(buildStatus((JSONObject) ob.get("status")));
                video.setStatistics(buildStatistic((JSONObject) ob.get("statistics")));
            } catch (NullPointerException e) {
                e.getMessage();
            }

            videoList.add(video);
        }

        return videoList;
    }

    private VideoStatistics buildStatistic(JSONObject statistic) {
        VideoStatistics videoStatistics = new VideoStatistics();

        videoStatistics.setViewCount(new BigInteger((String) statistic.get("viewCount")));
        videoStatistics.setLikeCount(new BigInteger((String) statistic.get("likeCount")));
        videoStatistics.setDislikeCount(new BigInteger((String) statistic.get("dislikeCount")));
        videoStatistics.setFavoriteCount(new BigInteger((String) statistic.get("favoriteCount")));
        videoStatistics.setCommentCount(new BigInteger((String) statistic.get("commentCount")));

        return videoStatistics;
    }

    private VideoStatus buildStatus(JSONObject status) {
        VideoStatus videoStatus = new VideoStatus();

        try {
            videoStatus.setEmbeddable((Boolean) status.get("embeddable"));
            videoStatus.setPublicStatsViewable((Boolean) status.get("publicStatsViewable"));
            videoStatus.setUploadStatus((String) status.get("uploadStatus"));
            videoStatus.setPrivacyStatus((String) status.get("privacyStatus"));
            videoStatus.setMadeForKids((Boolean) status.get("madeForKids"));
        } catch (NullPointerException e) {
            e.getMessage();
        }

        return videoStatus;
    }

    private VideoContentDetails buildContentDetails(JSONObject contentDetails) {
        VideoContentDetails videoContentDetails = new VideoContentDetails();

        videoContentDetails.setDuration((String) contentDetails.get("duration"));
        videoContentDetails.setDimension((String) contentDetails.get("dimension"));
        videoContentDetails.setDefinition((String) contentDetails.get("definition"));

        return videoContentDetails;
    }

    private VideoSnippet buildSnippet(JSONObject snippet) {
        VideoSnippet videoSnippet = new VideoSnippet();
        
        videoSnippet.setPublishedAt((String) snippet.get("publishedAt"));
        videoSnippet.setChannelId((String) snippet.get("channelId"));
        videoSnippet.setTitle((String) snippet.get("title"));
        videoSnippet.setDescription((String) snippet.get("description"));
        videoSnippet.setThumbnails(buildThumbnailDetails((JSONObject) snippet.get("thumbnails")));
        videoSnippet.setChannelTitle((String) snippet.get("channelTitle"));
        videoSnippet.setTags((List<String>) snippet.get("tags"));
        videoSnippet.setDefaultLanguage((String) snippet.get("defaultLanguage"));
        videoSnippet.setDefaultAudioLanguage((String) snippet.get("defaultAudioLanguage"));
        videoSnippet.setLocalized(buildLocalized((JSONObject) snippet.get("localized")));

        return videoSnippet;
    }

    private VideoLocalization buildLocalized(JSONObject localized) {
        VideoLocalization videoLocalization = new VideoLocalization();

        videoLocalization.setTitle((String) localized.get("title"));
        videoLocalization.setDescription((String) localized.get("description"));

        return videoLocalization;
    }

    private ThumbnailDetails buildThumbnailDetails(JSONObject thumbnails) {
        ThumbnailDetails thumbnailDetails = new ThumbnailDetails();

        thumbnailDetails.setDefault(buildThumbnail((JSONObject) thumbnails.get("default")));
        thumbnailDetails.setMedium(buildThumbnail((JSONObject) thumbnails.get("medium")));
        thumbnailDetails.setHigh(buildThumbnail((JSONObject) thumbnails.get("high")));
        thumbnailDetails.setStandard(buildThumbnail((JSONObject) thumbnails.get("standard")));
        thumbnailDetails.setMaxres(buildThumbnail((JSONObject) thumbnails.get("maxres")));

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
