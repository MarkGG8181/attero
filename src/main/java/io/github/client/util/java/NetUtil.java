package io.github.client.util.java;

import io.github.client.Attero;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NetUtil {
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static CompletableFuture<Void> download(String fileUrl, Path destinationPath) {
        var uri = URI.create(fileUrl);

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