package io.github.client.util.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.client.Attero;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class NetUtil {
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static URI sanitise(String rawUrl) {
        try {
            URL url = new URL(rawUrl);
            String base = url.getProtocol() + "://" + url.getHost() +
                    (url.getPort() != -1 ? ":" + url.getPort() : "") + url.getPath();

            String query = url.getQuery();
            if (query == null || query.isEmpty()) {
                return URI.create(base);
            }

            //encode each query parameter value only
            String sanitizedQuery = Arrays.stream(query.split("&"))
                    .map(param -> {
                        int eq = param.indexOf('=');
                        if (eq < 0) return param;
                        String key = param.substring(0, eq);
                        String value = param.substring(eq + 1);
                        return key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
                    })
                    .collect(Collectors.joining("&"));

            return URI.create(base + "?" + sanitizedQuery);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + rawUrl, e);
        }
    }

    public static JsonElement fetchJson(String query) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(sanitise(query))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("User-Agent", "NetUtil/1.0")
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch JSON from " + query + ": HTTP " + response.statusCode());
        }

        return JsonParser.parseString(response.body());
    }

    public static CompletableFuture<Void> download(String query, Path destinationPath) {
        var uri = sanitise(query);

        var request = HttpRequest
                .newBuilder(uri)
                .GET()
                .build();

        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofFile(destinationPath))
                .thenCompose(response -> {
                    var statusCode = response.statusCode();
                    if (statusCode == 200) {
                        Attero.LOGGER.info("Downloaded {}", destinationPath.getFileName());
                        return CompletableFuture.completedFuture(null);
                    } else if (statusCode == 302) {
                        var redirectUrl = response.headers().firstValue("Location").orElse(null);
                        if (redirectUrl != null) {
                            return download(redirectUrl, destinationPath);
                        } else {
                            return CompletableFuture.failedFuture(new RuntimeException("Failed to download file. HTTP 302 with no Location header."));
                        }
                    } else {
                        return CompletableFuture.failedFuture(new RuntimeException("Failed to download file. HTTP status code: " + statusCode));
                    }
                })
                .exceptionally(ex -> {
                    Attero.LOGGER.error("Failed to download file!", ex);
                    return null;
                });
    }
}