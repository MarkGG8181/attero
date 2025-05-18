package fag.ware.client.util.music;

import fag.ware.client.Fagware;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;

public class YtDlpDownloader {

    private static final String YTDLP_URL_WIN = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
    private static final String YTDLP_URL_UNIX = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp";

    private static final Path LOCAL_YTDLP_PATH = getMinecraftModFolder().resolve("yt-dlp" + (isWindows() ? ".exe" : ""));

    public static void ensureYtDlpExists() throws IOException {
        if (Files.exists(LOCAL_YTDLP_PATH)) {
            System.out.println("yt-dlp already downloaded");
            return;
        }

        Files.createDirectories(LOCAL_YTDLP_PATH.getParent());

        String downloadUrl = System.getProperty("os.name").toLowerCase().contains("win") ? YTDLP_URL_WIN : YTDLP_URL_UNIX;

        System.out.println("Downloading yt-dlp from: " + downloadUrl);

        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        InputStream input = connection.getInputStream();

        Files.copy(input, LOCAL_YTDLP_PATH, StandardCopyOption.REPLACE_EXISTING);
        input.close();

        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            // Make executable on Unix-based systems
            LOCAL_YTDLP_PATH.toFile().setExecutable(true);
        }

        System.out.println("yt-dlp downloaded to: " + LOCAL_YTDLP_PATH);
    }


    private static Path getMinecraftModFolder() {
        String appdata = System.getenv("APPDATA");
        if (appdata != null) {
            return Paths.get(appdata, ".minecraft", Fagware.MOD_ID);
        }

        // for linux/macOS, fallback to home/.minecraft/fagware
        String home = System.getProperty("user.home");
        return Paths.get(home, ".minecraft", Fagware.MOD_ID);
    }

    public static Path getYtDlpPath() {
        return LOCAL_YTDLP_PATH;
    }

    public static boolean isWindows() {
       return System.getProperty("os.name").toLowerCase().contains("win");
    }
}