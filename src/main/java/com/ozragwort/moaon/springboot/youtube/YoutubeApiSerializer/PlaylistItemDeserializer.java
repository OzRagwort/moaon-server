package com.ozragwort.moaon.springboot.youtube.YoutubeApiSerializer;

import com.google.api.services.youtube.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
part :  O   id
        O   snippet
        O   contentDetails
        O   status
 */
@Component
public class PlaylistItemDeserializer {

    public PlaylistItemListResponse deserialize(JSONObject jsonObject) {
        PlaylistItemListResponse playlistItemListResponse = new PlaylistItemListResponse();

        playlistItemListResponse.setItems(buildItems((JSONArray) jsonObject.get("items")));
        playlistItemListResponse.setNextPageToken((String) jsonObject.get("nextPageToken"));
        playlistItemListResponse.setPageInfo(buildPageInfo((JSONObject) jsonObject.get("pageInfo")));
        playlistItemListResponse.setEtag((String) jsonObject.get("etag"));
        playlistItemListResponse.setKind((String) jsonObject.get("kind"));
        playlistItemListResponse.setPrevPageToken((String) jsonObject.get("prevPageToken"));

        return playlistItemListResponse;
    }

    private PageInfo buildPageInfo(JSONObject jsonObject) {
        PageInfo pageInfo = new PageInfo();

        pageInfo.setTotalResults(((Long) jsonObject.get("totalResults")).intValue());
        pageInfo.setResultsPerPage(((Long) jsonObject.get("resultsPerPage")).intValue());

        return pageInfo;
    }

    private List<PlaylistItem> buildItems(JSONArray items) {
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        PlaylistItem playlistItem;

        for (int i = 0 ; i < items.size() ; i++) {
            playlistItem = new PlaylistItem();

            JSONObject ob = (JSONObject) items.get(i);

            try {
                playlistItem.setSnippet(buildSnippet((JSONObject) ob.get("snippet")));
                playlistItem.setContentDetails(buildContentDetails((JSONObject) ob.get("contentDetails")));
                playlistItem.setEtag((String) ob.get("etag"));
                playlistItem.setId((String) ob.get("id"));
                playlistItem.setKind((String) ob.get("kind"));
                playlistItem.setStatus(buildStatus((JSONObject) ob.get("status")));
            } catch (NullPointerException e) {
                e.getMessage();
            }

            playlistItemList.add(playlistItem);
        }

        return playlistItemList;
    }

    private PlaylistItemStatus buildStatus(JSONObject status) {
        PlaylistItemStatus playlistItemStatus = new PlaylistItemStatus();

        playlistItemStatus.setPrivacyStatus((String) status.get("privacyStatus"));

        return playlistItemStatus;
    }

    private PlaylistItemContentDetails buildContentDetails(JSONObject contentDetails) {
        PlaylistItemContentDetails playlistItemContentDetails = new PlaylistItemContentDetails();

        playlistItemContentDetails.setVideoId((String) contentDetails.get("videoId"));
        playlistItemContentDetails.setVideoPublishedAt((String) contentDetails.get("videoPublishedAt"));

        return playlistItemContentDetails;
    }

    private PlaylistItemSnippet buildSnippet(JSONObject snippet) {
        PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();

        playlistItemSnippet.setTitle((String) snippet.get("title"));
        playlistItemSnippet.setDescription((String) snippet.get("description"));
        playlistItemSnippet.setChannelId((String) snippet.get("channelId"));
        playlistItemSnippet.setChannelTitle((String) snippet.get("channelTitle"));
        playlistItemSnippet.setPlaylistId((String) snippet.get("playlistId"));
        playlistItemSnippet.setPosition((Long) snippet.get("position"));
        playlistItemSnippet.setPublishedAt((String) snippet.get("publishedAt"));
        playlistItemSnippet.setResourceId(buildResourceId((JSONObject) snippet.get("resourceId")));
        playlistItemSnippet.setThumbnails(buildThumbnailDetails((JSONObject) snippet.get("thumbnails")));

        return playlistItemSnippet;
    }

    private ResourceId buildResourceId(JSONObject resource) {
        ResourceId resourceId = new ResourceId();

        resourceId.setKind((String) resource.get("kink"));
        resourceId.setVideoId((String) resource.get("videoId"));

        return resourceId;
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
