package fag.ware.client.util.client;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class BuildInfoRetriever implements BuildInfoService {

    @Override
    @SneakyThrows
    public BuildInfo git() {
        final Properties properties = new Properties();
        final InputStream inputStream = BuildInfoRetriever.class.getClassLoader().getResourceAsStream("git.properties");
        if (Objects.isNull(inputStream))
            return this.empty();

        properties.load(inputStream);

        return BuildInfo.builder()
                .branch(properties.getProperty("git.branch"))
                .version(properties.getProperty("git.build.version"))
                .commits(Integer.parseInt(properties.getProperty("git.total.commit.count")))
                .commitId(properties.getProperty("git.commit.id"))
                .commitIdAbbreviation(properties.getProperty("git.commit.id.abbrev"))
                .timestamp(properties.getProperty("git.commit.time"))
                .build();
    }

    @Override
    public BuildInfo empty() {
        return BuildInfo.builder()
                .branch("none")
                .version("0.0.0")
                .commits(0)
                .commitId("N/A")
                .commitIdAbbreviation("N/A")
                .timestamp("N/A")
                .build();
    }
}