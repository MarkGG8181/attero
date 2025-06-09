package fag.ware.client.util.client.git;

import lombok.Getter;

@Getter
public class BuildInfo {
    private String branch;
    private String version;
    private int commits;
    private String commitId;
    private String commitIdAbbreviation;
    private String timestamp;

    private BuildInfo() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final BuildInfo info = new BuildInfo();

        public Builder branch(String branch) {
            info.branch = branch;
            return this;
        }

        public Builder version(String version) {
            info.version = version;
            return this;
        }

        public Builder commits(int commits) {
            info.commits = commits;
            return this;
        }

        public Builder commitId(String commitId) {
            info.commitId = commitId;
            return this;
        }

        public Builder commitIdAbbreviation(String commitIdAbbreviation) {
            info.commitIdAbbreviation = commitIdAbbreviation;
            return this;
        }

        public Builder timestamp(String timestamp) {
            info.timestamp = timestamp;
            return this;
        }

        public BuildInfo build() {
            return info;
        }
    }
}