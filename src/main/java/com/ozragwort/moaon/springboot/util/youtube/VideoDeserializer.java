package com.ozragwort.moaon.springboot.util.youtube;

import com.google.api.services.youtube.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        for (Object item : items) {
            video = new Video();

            JSONObject ob = (JSONObject) item;

            video.setKind((String) ob.get("kind"));
            video.setEtag((String) ob.get("etag"));
            video.setId((String) ob.get("id"));
            video.setSnippet(buildSnippet((JSONObject) ob.get("snippet")));
            video.setContentDetails(buildContentDetails((JSONObject) ob.get("contentDetails")));
            video.setStatistics(buildStatistic((JSONObject) ob.get("statistics")));

            videoList.add(video);
        }

        return videoList;
    }

    private VideoStatistics buildStatistic(JSONObject statistic) {
        VideoStatistics videoStatistics = new VideoStatistics();

        BigInteger vc = new BigInteger((String) statistic.get("viewCount"));
        BigInteger cc = statistic.containsKey("commentCount")
                ? new BigInteger((String) statistic.get("commentCount"))
                : BigInteger.ZERO;
        BigInteger lc = statistic.containsKey("likeCount")
                ? new BigInteger((String) statistic.get("likeCount"))
                : BigInteger.ZERO;
        BigInteger dc = statistic.containsKey("dislikeCount")
                ? new BigInteger((String) statistic.get("dislikeCount"))
                : BigInteger.ZERO;

        videoStatistics.setViewCount(vc);
        videoStatistics.setCommentCount(cc);
        videoStatistics.setLikeCount(lc);
        videoStatistics.setDislikeCount(dc);

        return videoStatistics;
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

        List<String> tags;
        try {
            tags = ((List<String>) snippet.get("tags")).stream().map(String::new).distinct().collect(Collectors.toList());
        } catch (NullPointerException e) {
            tags = new ArrayList<>();
        }

        videoSnippet.setPublishedAt((String) snippet.get("publishedAt"));
        videoSnippet.setChannelId((String) snippet.get("channelId"));
        videoSnippet.setTitle((String) snippet.get("title"));
        videoSnippet.setDescription((String) snippet.get("description"));
        videoSnippet.setThumbnails(buildThumbnailDetails((JSONObject) snippet.get("thumbnails")));
        videoSnippet.setChannelTitle((String) snippet.get("channelTitle"));
        videoSnippet.setTags(tags);
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
