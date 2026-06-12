package com.carbonclient.update;

public final class UpdateInfo {

    private final String version;
    private final String downloadUrl;

    public UpdateInfo(String version, String downloadUrl) {
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("Version cannot be empty.");
        }
        if (downloadUrl == null || downloadUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Download URL cannot be empty.");
        }

        this.version = version;
        this.downloadUrl = downloadUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
