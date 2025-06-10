package fag.ware.client.util.client.git;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuildInfo {
    private final String branch;
    private final String version;
    private final int commits;
    private final String commitId;
    private final String commitIdAbbreviation;
    private final String timestamp;
}