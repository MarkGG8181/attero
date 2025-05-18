package fag.ware.client.util.music;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fag.ware.client.Fagware;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeThumbnailParser {
    public static YoutubeSong[] getVideosFromPlaylist(String playlistId) {
        return extractVideosFromHTML(getRawHTMLFromURL("https://www.youtube.com/playlist?list=" + playlistId + "&disable_polymer=1"));
    }

    public static YoutubeSong[] extractVideosFromHTML(String htmlContent) {
        List<YoutubeSong> results = new ArrayList<>();

        // Extract ytInitialData JSON from the HTML content
        Pattern ytInitialDataPattern = Pattern.compile("var ytInitialData = (\\{.*?\\});");
        Matcher matcher = ytInitialDataPattern.matcher(htmlContent);

        if (!matcher.find()) {
            Fagware.LOGGER.error("ytInitialData not found in HTML.");
            return new YoutubeSong[0];
        }

        String ytInitialDataRaw = matcher.group(1);

        try {
            JsonElement root = JsonParser.parseString(ytInitialDataRaw);
            JsonObject rootObj = root.getAsJsonObject();

            // Traverse the object to locate videos in a playlist
            JsonArray contents = rootObj
                    .getAsJsonObject("contents")
                    .getAsJsonObject("twoColumnBrowseResultsRenderer")
                    .getAsJsonArray("tabs")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("tabRenderer")
                    .getAsJsonObject("content")
                    .getAsJsonObject("sectionListRenderer")
                    .getAsJsonArray("contents")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("itemSectionRenderer")
                    .getAsJsonArray("contents")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("playlistVideoListRenderer")
                    .getAsJsonArray("contents");

            for (JsonElement videoElem : contents) {
                JsonObject videoObj = videoElem.getAsJsonObject().getAsJsonObject("playlistVideoRenderer");

                if (videoObj == null) continue;

                String videoId = videoObj.get("videoId").getAsString();
                String title = videoObj.getAsJsonObject("title")
                        .getAsJsonArray("runs")
                        .get(0).getAsJsonObject()
                        .get("text").getAsString();

                title = title.replaceAll("\\s*[\\[\\(\\{].*?[\\]\\)\\}]", "").trim();

                if (title.contains(" - ")) {
                    title = title.substring(title.indexOf(" - ") + 3).trim();
                }

                title = title.replaceAll("(?i)\\s+feat\\..*", "").replaceAll("(?i)\\s+ft\\..*", "").trim();

                String artist = "";
                if (videoObj.has("shortBylineText")) {
                    JsonObject shortByline = videoObj.getAsJsonObject("shortBylineText");
                    JsonArray runs = shortByline.getAsJsonArray("runs");
                    if (runs != null && runs.size() > 0) {
                        JsonObject run = runs.get(0).getAsJsonObject();
                        artist = run.get("text").getAsString();
                    }
                }

                results.add(new YoutubeSong(title, artist, videoId));
            }

        } catch (Exception e) {
            Fagware.LOGGER.error("Failed to parse ytInitialData", e);
        }

        return results.toArray(new YoutubeSong[0]);
    }

    private static String getRawHTMLFromURL(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.addHeader("ChatCommandExecutor-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

        try {
            CloseableHttpResponse response = client.execute(get);
            Throwable throwable = null;

            String s;
            try {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return "";
                }

                s = EntityUtils.toString(entity);
            } catch (Throwable t) {
                throwable = t;
                throw t;
            } finally {
                if (response != null) {
                    if (throwable != null) {
                        try {
                            response.close();
                        } catch (Throwable t) {
                            throwable.addSuppressed(t);
                        }
                    } else {
                        response.close();
                    }
                }
            }

            return s;
        } catch (IllegalStateException | IOException | ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String decodeURL(String url) {
        String fullUrl;

        try {
            fullUrl = StringEscapeUtils.unescapeHtml4(URLDecoder.decode(url, StandardCharsets.UTF_8)).trim();
        } catch (Exception e) {
            Fagware.LOGGER.error("Failed to decode url", e);
            return "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        }

        return fullUrl;
    }

    public static URL getFullURL(String videoID) {
        try {
            return new URL("https://www.youtube.com/watch?v=" + videoID);
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to get the video's full url", e);
            return null;
        }
    }
}