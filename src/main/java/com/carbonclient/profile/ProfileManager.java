package com.carbonclient.profile;

import com.carbonclient.config.ConfigManager;
import com.carbonclient.notification.NotificationManager;
import com.google.gson.JsonObject;
import java.util.List;
import org.apache.logging.log4j.Logger;

public final class ProfileManager {

    public static final String DEFAULT_PROFILE_NAME = "Default";

    private final ProfileStorage storage;
    private final ConfigManager configManager;
    private final NotificationManager notificationManager;
    private final Logger logger;
    private String activeProfileName = DEFAULT_PROFILE_NAME;

    public ProfileManager(
        ProfileStorage storage,
        ConfigManager configManager,
        NotificationManager notificationManager,
        Logger logger
    ) {
        if (storage == null
            || configManager == null
            || notificationManager == null
            || logger == null) {
            throw new IllegalArgumentException(
                "ProfileManager dependencies cannot be null."
            );
        }

        this.storage = storage;
        this.configManager = configManager;
        this.notificationManager = notificationManager;
        this.logger = logger;
    }

    public void initialize() {
        if (findProfile(DEFAULT_PROFILE_NAME) == null) {
            writeProfileSilently(
                DEFAULT_PROFILE_NAME,
                configManager.createSnapshot()
            );
        }

        String storedActive = sanitizeName(storage.readActiveProfileName());
        if (storedActive == null || findProfile(storedActive) == null) {
            storedActive = DEFAULT_PROFILE_NAME;
        }

        if (!loadProfile(storedActive, false)) {
            activeProfileName = DEFAULT_PROFILE_NAME;
            loadProfile(DEFAULT_PROFILE_NAME, false);
        }
    }

    public List<Profile> getProfiles() {
        return storage.listProfiles();
    }

    public Profile getActiveProfile() {
        return findProfile(activeProfileName);
    }

    public String getActiveProfileName() {
        return activeProfileName;
    }

    public boolean createProfile(String requestedName) {
        String name = sanitizeName(requestedName);
        if (!isAvailableName(name)) {
            return false;
        }

        if (!writeProfile(name, configManager.createDefaultSnapshot())) {
            return false;
        }
        notificationManager.success(
            "Profile Created",
            "Created clean profile: " + name
        );
        return true;
    }

    public boolean saveActiveProfile() {
        if (!writeProfile(activeProfileName, configManager.createSnapshot())) {
            return false;
        }
        notificationManager.success(
            "Profile Saved",
            activeProfileName + " was saved."
        );
        return true;
    }

    public boolean loadProfile(String requestedName) {
        return loadProfile(requestedName, true);
    }

    public boolean duplicateProfile(String sourceName, String requestedName) {
        Profile source = findProfile(sourceName);
        String name = sanitizeName(requestedName);
        if (source == null || !isAvailableName(name)) {
            return false;
        }

        try {
            storage.writeProfile(name, storage.readProfile(source));
            notificationManager.success(
                "Profile Duplicated",
                "Duplicated profile: " + name
            );
            return true;
        } catch (Exception exception) {
            reportFailure("duplicate", source.getName(), exception);
            return false;
        }
    }

    public boolean duplicateActiveProfile(String requestedName) {
        String name = sanitizeName(requestedName);
        if (!isAvailableName(name)) {
            return false;
        }

        if (!writeProfile(name, configManager.createSnapshot())) {
            return false;
        }
        notificationManager.success(
            "Profile Duplicated",
            "Duplicated profile: " + name
        );
        return true;
    }

    public boolean renameProfile(String sourceName, String requestedName) {
        Profile source = findProfile(sourceName);
        String name = sanitizeName(requestedName);
        if (source == null
            || DEFAULT_PROFILE_NAME.equalsIgnoreCase(source.getName())
            || !isAvailableName(name)) {
            return false;
        }

        try {
            JsonObject snapshot = storage.readProfile(source);
            storage.writeProfile(name, snapshot);
            if (!storage.deleteProfile(source)) {
                throw new IllegalStateException("Could not remove old profile.");
            }
            if (source.getName().equalsIgnoreCase(activeProfileName)) {
                activeProfileName = name;
                storage.writeActiveProfileName(activeProfileName);
            }
            notificationManager.success(
                "Profile Renamed",
                source.getName() + " renamed to " + name + "."
            );
            return true;
        } catch (Exception exception) {
            reportFailure("rename", source.getName(), exception);
            return false;
        }
    }

    public boolean deleteProfile(String requestedName) {
        Profile profile = findProfile(requestedName);
        if (profile == null
            || DEFAULT_PROFILE_NAME.equalsIgnoreCase(profile.getName())) {
            return false;
        }

        if (!storage.deleteProfile(profile)) {
            notificationManager.error(
                "Profile Delete Failed",
                profile.getName() + " could not be deleted."
            );
            return false;
        }

        if (profile.getName().equalsIgnoreCase(activeProfileName)) {
            loadProfile(DEFAULT_PROFILE_NAME, false);
        }
        notificationManager.success(
            "Profile Deleted",
            profile.getName() + " was deleted."
        );
        return true;
    }

    public String sanitizeName(String requestedName) {
        if (requestedName == null) {
            return null;
        }

        String name = requestedName
            .replaceAll("[\\\\/:*?\"<>|]", "")
            .replaceAll("[\\p{Cntrl}]", "")
            .trim()
            .replaceAll("\\s+", " ");
        while (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1).trim();
        }
        if (name.isEmpty()) {
            return null;
        }
        if (name.length() > 32) {
            name = name.substring(0, 32).trim();
        }
        return name.isEmpty() ? null : name;
    }

    private boolean loadProfile(String requestedName, boolean notify) {
        Profile profile = findProfile(requestedName);
        if (profile == null) {
            return false;
        }

        try {
            JsonObject snapshot = storage.readProfile(profile);
            configManager.applySnapshot(snapshot);
            configManager.save();
            storage.writeProfile(
                profile.getName(),
                configManager.createSnapshot()
            );
            activeProfileName = profile.getName();
            storage.writeActiveProfileName(activeProfileName);
            if (notify) {
                notificationManager.success(
                    "Profile Loaded",
                    "Profile Loaded: " + activeProfileName
                );
            }
            return true;
        } catch (Exception exception) {
            reportFailure("load", profile.getName(), exception);
            return false;
        }
    }

    private boolean writeProfile(String name, JsonObject snapshot) {
        try {
            storage.writeProfile(name, snapshot);
            return true;
        } catch (Exception exception) {
            reportFailure("save", name, exception);
            return false;
        }
    }

    private void writeProfileSilently(String name, JsonObject snapshot) {
        try {
            storage.writeProfile(name, snapshot);
        } catch (Exception exception) {
            reportFailure("create", name, exception);
        }
    }

    private boolean isAvailableName(String name) {
        return name != null && findProfile(name) == null;
    }

    private Profile findProfile(String name) {
        if (name == null) {
            return null;
        }

        for (Profile profile : storage.listProfiles()) {
            if (profile.getName().equalsIgnoreCase(name)) {
                return profile;
            }
        }
        return null;
    }

    private void reportFailure(
        String operation,
        String profileName,
        Exception exception
    ) {
        logger.warn(
            "Could not {} Carbon profile {}.",
            operation,
            profileName,
            exception
        );
        notificationManager.error(
            "Profile Error",
            "Could not " + operation + " " + profileName + "."
        );
    }
}
