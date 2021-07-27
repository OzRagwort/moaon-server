package com.ozragwort.moaon.springboot.util.youtube;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Component
public class YoutubeDataApi {

    private final String CLIENT_SECRETS = "json/yt_api_key.json";

    private String getCLIENT_SECRETS() {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            ClassPathResource resource = new ClassPathResource(CLIENT_SECRETS);
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String jsonTxt = new String(byteArray, StandardCharsets.UTF_8);
            jsonObject = (JSONObject) jsonParser.parse(jsonTxt);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return (String) jsonObject.get("api_key");
    }

    private JSONObject get(String strUrl) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = new JSONObject();

            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            con.setRequestMethod("GET");

            con.setDoOutput(false);

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                jsonObject = (JSONObject) parser.parse(br);
            } else {
                Logger.getGlobal().warning("YoutubeApiError : [" + strUrl + "] => " + con.getResponseMessage());
            }
            return jsonObject;

        } catch (Exception e) {
            Logger.getGlobal().warning("YoutubeApiError : [" + strUrl + "] => " + e.toString());
            return null;
        }
    }

    public ChannelListResponse getChannelListResponse(String channelId, String secret) {
        String secretKey = makeKey(secret);

        String part = "snippet,contentDetails,statistics,brandingSettings";
        String url = "https://www.googleapis.com/youtube/v3/channels" +
                "?key=" + secretKey +
                "&part=" + part +
                "&id=" + channelId;

        JSONObject jsonObject = get(url);

        return setChannelList(jsonObject);
    }

    public VideoListResponse getVideoListResponse(String videoId, String secret) {
        String secretKey = makeKey(secret);

        String part = "snippet,contentDetails,statistics";
        String url = "https://www.googleapis.com/youtube/v3/videos" +
                "?key=" + secretKey +
                "&part=" + part +
                "&id=" + videoId;

        JSONObject jsonObject = get(url);

        return setVideoList(jsonObject);
    }

    public List<PlaylistItemListResponse> getPlaylistItemListResponse(String uploadsList, String secret) {
        String secretKey = makeKey(secret);

        List<PlaylistItemListResponse> listResponses = new JSONArray();
        PlaylistItemListResponse playlistItemListResponse;

        String part = "snippet,contentDetails,status";
        String nextPageToken = "";

        do {
            String url = "https://www.googleapis.com/youtube/v3/playlistItems" +
                    "?key=" + secretKey +
                    "&part=" + part +
                    "&playlistId=" + uploadsList +
                    "&maxResults=" + 50 +
                    "&pageToken=" + nextPageToken;

            JSONObject jsonObject = get(url);
            playlistItemListResponse = setPlaylistItemList(jsonObject);
            nextPageToken = playlistItemListResponse.getNextPageToken();
            listResponses.add(playlistItemListResponse);
        } while (nextPageToken != null);

        return listResponses;
    }

    private ChannelListResponse setChannelList(JSONObject jsonObject) {
        ChannelListResponse channelListResponse;
        ChannelDeserializer channelDeserializer = new ChannelDeserializer();
        channelListResponse = channelDeserializer.deserialize(jsonObject);
        return channelListResponse;
    }

    private PlaylistItemListResponse setPlaylistItemList(JSONObject jsonObject) {
        PlaylistItemListResponse playlistItemListResponse;
        PlaylistItemDeserializer playlistItemDeserializer = new PlaylistItemDeserializer();
        playlistItemListResponse = playlistItemDeserializer.deserialize(jsonObject);
        return playlistItemListResponse;
    }

    private VideoListResponse setVideoList(JSONObject jsonObject) {
        VideoListResponse videoListResponse;
        VideoDeserializer videoDeserializer = new VideoDeserializer();
        videoListResponse = videoDeserializer.deserialize(jsonObject);
        return videoListResponse;
    }

    private String makeKey(String secret) {
        if (secret == null) {
            return getCLIENT_SECRETS();
        } else {
            return secret;
        }
    }

}
