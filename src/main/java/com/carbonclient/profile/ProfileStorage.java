package com.carbonclient.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ProfileStorage {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private static final String METADATA_FILE_NAME = "metadata.json";

    private final File profilesDirectory;
    private final File metadataFile;

    public ProfileStorage(File gameDirectory) {
        if (gameDirectory == null) {
            throw new IllegalArgumentException("Game directory cannot be null.");
        }

        this.profilesDirectory = new File(
            new File(gameDirectory, "carbon"),
            "profiles"
        );
        this.metadataFile = new File(profilesDirectory, METADATA_FILE_NAME);
    }

    public List<Profile> listProfiles() {
        File[] files = profilesDirectory.listFiles();
        List<Profile> profiles = new ArrayList<Profile>();
        if (files == null) {
            return profiles;
        }

        for (File file : files) {
            if (!file.isFile()
                || !file.getName().toLowerCase().endsWith(".json")
                || METADATA_FILE_NAME.equalsIgnoreCase(file.getName())) {
                continue;
            }

            String name = file.getName().substring(
                0,
                file.getName().length() - 5
            );
            profiles.add(new Profile(name, file, file.lastModified()));
        }

        Collections.sort(
            profiles,
            new Comparator<Profile>() {
                @Override
                public int compare(Profile first, Profile second) {
                    if ("Default".equalsIgnoreCase(first.getName())) {
                        return -1;
                    }
                    if ("Default".equalsIgnoreCase(second.getName())) {
                        return 1;
                    }
                    return first.getName().compareToIgnoreCase(second.getName());
                }
            }
        );
        return profiles;
    }

    public JsonObject readProfile(Profile profile) throws Exception {
        try (Reader reader = new InputStreamReader(
            new FileInputStream(profile.getFile()),
            StandardCharsets.UTF_8
        )) {
            JsonElement root = new JsonParser().parse(reader);
            if (!root.isJsonObject()) {
                throw new IllegalStateException("Profile root is not an object.");
            }
            return root.getAsJsonObject();
        }
    }

    public void writeProfile(String name, JsonObject snapshot) throws Exception {
        ensureDirectory();
        writeAtomically(getProfileFile(name), snapshot);
    }

    public boolean deleteProfile(Profile profile) {
        return profile != null
            && profile.getFile().isFile()
            && profile.getFile().delete();
    }

    public String readActiveProfileName() {
        if (!metadataFile.isFile()) {
            return "Default";
        }

        try (Reader reader = new InputStreamReader(
            new FileInputStream(metadataFile),
            StandardCharsets.UTF_8
        )) {
            JsonElement root = new JsonParser().parse(reader);
            if (!root.isJsonObject()) {
                return "Default";
            }
            JsonElement active = root.getAsJsonObject().get("activeProfile");
            return active != null && active.isJsonPrimitive()
                ? active.getAsString()
                : "Default";
        } catch (Exception ignored) {
            return "Default";
        }
    }

    public void writeActiveProfileName(String name) throws Exception {
        JsonObject metadata = new JsonObject();
        metadata.addProperty("activeProfile", name);
        ensureDirectory();
        writeAtomically(metadataFile, metadata);
    }

    public File getProfilesDirectory() {
        return profilesDirectory;
    }

    private File getProfileFile(String name) {
        return new File(profilesDirectory, name + ".json");
    }

    private void ensureDirectory() {
        if (!profilesDirectory.exists() && !profilesDirectory.mkdirs()) {
            throw new IllegalStateException(
                "Could not create profile directory: " + profilesDirectory
            );
        }
    }

    private void writeAtomically(File target, JsonObject root) throws Exception {
        File temporaryFile = new File(
            target.getParentFile(),
            target.getName() + ".tmp"
        );

        try (Writer writer = new OutputStreamWriter(
            new FileOutputStream(temporaryFile),
            StandardCharsets.UTF_8
        )) {
            GSON.toJson(root, writer);
        }

        try {
            Files.move(
                temporaryFile.toPath(),
                target.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            );
        } catch (Exception exception) {
            Files.move(
                temporaryFile.toPath(),
                target.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );
        }
    }
}
