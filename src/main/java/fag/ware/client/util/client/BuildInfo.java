package fag.ware.client.util.client;

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

    // Getters if needed
    public String getBranch() {
        return branch;
    }

    public String getVersion() {
        return version;
    }

    public int getCommits() {
        return commits;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getCommitIdAbbreviation() {
        return commitIdAbbreviation;
    }

    public String getTimestamp() {
        return timestamp;
    }
}