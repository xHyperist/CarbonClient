package com.carbonclient.profile;

import java.io.File;

public final class Profile {

    private final String name;
    private final File file;
    private final long lastModified;

    public Profile(String name, File file, long lastModified) {
        if (name == null || name.trim().isEmpty() || file == null) {
            throw new IllegalArgumentException(
                "Profile name and file cannot be empty."
            );
        }

        this.name = name;
        this.file = file;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public long getLastModified() {
        return lastModified;
    }
}
