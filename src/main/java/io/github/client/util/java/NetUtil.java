package io.github.client.util.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NetUtil {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static JsonElement fetchJson(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .header("Accept", "application/json")
                .header("User-Agent", "NetUtil/1.0")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch JSON from " + url + ": HTTP " + response.statusCode());
        }

        return JsonParser.parseString(response.body());
    }
}
