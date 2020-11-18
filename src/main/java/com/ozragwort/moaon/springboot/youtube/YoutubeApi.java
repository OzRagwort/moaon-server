package com.ozragwort.moaon.springboot.youtube;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.youtube.model.*;
import com.google.gson.Gson;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.youtube.YoutubeApiSerializer.ChannelDeserializer;
import com.ozragwort.moaon.springboot.youtube.YoutubeApiSerializer.PlaylistItemDeserializer;
import com.ozragwort.moaon.springboot.youtube.YoutubeApiSerializer.VideoDeserializer;
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
public class YoutubeApi {

    private final String CLIENT_SECRETS = "json/yt_api_key.json";

    private String getCLIENT_SECRETS() {
        FileReader fileReader = null;

        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = null;

        try {

            ClassPathResource resource = new ClassPathResource(CLIENT_SECRETS);

            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());

            String jsonTxt = new String(byteArray, StandardCharsets.UTF_8);

            jsonObject = (JSONObject) jsonParser.parse(jsonTxt);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    public ChannelListResponse getChannelListResponse(String channelId) {

        String part = "snippet,contentDetails,statistics";

        String url = "https://www.googleapis.com/youtube/v3/channels" +
                "?key=" + getCLIENT_SECRETS() +
                "&part=" + part +
                "&id=" + channelId;

        JSONObject jsonObject = get(url);

        ChannelListResponse channelListResponse = setChannelList(jsonObject);

        return channelListResponse;
    }

    public VideoListResponse getVideoListResponse(String videoId) {

        String part = "snippet,contentDetails,statistics,status";

        String url = "https://www.googleapis.com/youtube/v3/videos" +
                "?key=" + getCLIENT_SECRETS() +
                "&part=" + part +
                "&id=" + videoId;

        JSONObject jsonObject = get(url);

        VideoListResponse videoListResponse = setVideoList(jsonObject);

        return videoListResponse;
    }

    public List<PlaylistItemListResponse> getPlaylistItemListResponse(String uploadsList) {

        List<PlaylistItemListResponse> listResponses = new JSONArray();
        PlaylistItemListResponse playlistItemListResponse;

        String part = "snippet,contentDetails";
        String nextPageToken = "";

        do {
            String url = "https://www.googleapis.com/youtube/v3/playlistItems" +
                    "?key=" + getCLIENT_SECRETS() +
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




























    private InputStream gettest(String strUrl) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = new JSONObject();

            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            con.setRequestMethod("GET");

            con.setDoOutput(false);

//            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(con.getInputStream(), "utf-8"));
//                jsonObject = (JSONObject) parser.parse(br);
//            } else {
//                Logger.getGlobal().warning("YoutubeApiError : [" + strUrl + "] => " + con.getResponseMessage());
//            }
//            return jsonObject;

            return con.getInputStream();

        } catch (Exception e) {
            Logger.getGlobal().warning("YoutubeApiError : [" + strUrl + "] => " + e.toString());
            return null;
        }
    }

















}
