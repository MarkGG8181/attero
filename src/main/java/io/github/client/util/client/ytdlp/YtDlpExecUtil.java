package io.github.client.util.client.ytdlp;

import com.jfposton.ytdlp.YtDlp;
import io.github.client.Attero;
import io.github.client.util.java.FileUtil;
import io.github.client.util.java.NetUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@SuppressWarnings("all")
public class YtDlpExecUtil {
    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private static Path executable = FileUtil.CLIENT_DIR.resolve("music"); //default to the python platform independent binary

    static {
        if (OS_NAME.contains("win")) executable = executable.resolve("yt-dlp.exe");
        else if (OS_NAME.contains("mac")) executable = executable.resolve("yt-dlp_macos");
        else if (OS_NAME.contains("linux")) executable = executable.resolve("yt-dlp_linux");

        YtDlp.setExecutablePath(FileUtil.CLIENT_DIR.resolve(executable).toString());
    }

    public static void download() {
        NetUtil.download("https://github.com/yt-dlp/yt-dlp/releases/latest/download/SHA2-256SUMS", executable.getParent().resolve("hashes"));

        if (!Files.exists(executable)) {
            Attero.LOGGER.info("Downloading yt-dlp executable...");
            downloadYtDLP();
        } else {
            Attero.LOGGER.debug("Comparing yt-dlp executable hash...");
            var sha256 = FileUtil.getSHA256(executable);
            var hashLines = FileUtil.readAllLines(executable.getParent().resolve("hashes"));

            boolean hashFound = Arrays.stream(hashLines)
                    .map(String::trim)
                    .anyMatch(line -> line.startsWith(sha256 + " "));

            if (!hashFound) {
                Attero.LOGGER.warn("Redownloading yt-dlp, hash not found in hashes file\nComputed Hash: {}", sha256);
                downloadYtDLP();
            } else {
                Attero.LOGGER.info("No yt-dlp update required.");
            }
        }

        if (!Files.isExecutable(executable)) {
            Attero.LOGGER.warn("yt-dlp is not executable! Attempting to fix...");
            executable.toFile().setExecutable(true);
        }
    }

    private static void downloadYtDLP() {
        Path downloadPath = Path.of(YtDlp.getExecutablePath());

        NetUtil.download("https://github.com/yt-dlp/yt-dlp/releases/latest/download/" + executable.getFileName(), downloadPath)
                .thenApply(v -> {
                    Attero.LOGGER.debug("Downloaded yt-dlp successfully.");

                    if (!OS_NAME.contains("win")) {
                        try {
                            downloadPath.toFile().setExecutable(true);
                            Runtime.getRuntime().exec(new String[]{"chmod", "+x", downloadPath.toString()});
                            Attero.LOGGER.info("Set yt-dlp as executable.");
                        } catch (Exception e) {
                            Attero.LOGGER.error("Failed to set yt-dlp executable permission", e);
                        }
                    }

                    return null;
                });
    }
}