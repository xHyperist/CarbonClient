package com.carbonclient.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public final class CarbonTweaker implements ITweaker {

    private static final String LAUNCH_TARGET = "net.minecraft.client.main.Main";
    private static final String DEFAULT_PROFILE = "1.8.9";
    private static final String DEFAULT_USERNAME = "Player";
    private static final String DEFAULT_UUID = "00000000-0000-0000-0000-000000000000";

    private final List<String> launchArguments = new ArrayList<String>();
    private File gameDir;
    private File assetsDir;
    private String profile;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.launchArguments.clear();

        if (args != null) {
            this.launchArguments.addAll(args);
        }

        this.gameDir = gameDir;
        this.assetsDir = assetsDir;
        this.profile = profile;

        System.out.println("[Carbon Client] CarbonTweaker acceptOptions argsCount=" + this.launchArguments.size());
        System.out.println("[Carbon Client] CarbonTweaker gameDir=" + safePath(gameDir));
        System.out.println("[Carbon Client] CarbonTweaker assetsDir=" + safePath(assetsDir));
        System.out.println("[Carbon Client] CarbonTweaker profile=" + safeValue(profile));
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        try {
            CarbonBootstrap.markLauncherMode();
            CarbonBootstrap.bootstrap();
            System.out.println("[Carbon Client] CarbonTweaker injected. gameDir=" + safePath(gameDir)
                + ", assetsDir=" + safePath(assetsDir)
                + ", profile=" + safeValue(profile));
        } catch (Throwable throwable) {
            System.err.println("[Carbon Client] CarbonTweaker bootstrap failed: " + throwable.getMessage());
        }
    }

    @Override
    public String getLaunchTarget() {
        return LAUNCH_TARGET;
    }

    @Override
    public String[] getLaunchArguments() {
        List<String> finalArguments = new ArrayList<String>(launchArguments);
        File resolvedGameDir = resolveGameDir();
        File resolvedAssetsDir = resolveAssetsDir(resolvedGameDir);
        String resolvedProfile = resolveProfile();

        putIfMissing(finalArguments, "--version", resolvedProfile);
        putIfMissing(finalArguments, "--gameDir", resolvedGameDir.getAbsolutePath());
        putIfMissing(finalArguments, "--assetsDir", resolvedAssetsDir.getAbsolutePath());
        putIfMissing(finalArguments, "--assetIndex", "1.8");
        putIfMissing(finalArguments, "--username", fallback(getOptionValue(finalArguments, "--username"), DEFAULT_USERNAME));
        putIfMissing(finalArguments, "--uuid", fallback(getOptionValue(finalArguments, "--uuid"), DEFAULT_UUID));
        putIfMissing(finalArguments, "--accessToken", fallback(getOptionValue(finalArguments, "--accessToken"), "0"));
        putIfMissing(finalArguments, "--userType", fallback(getOptionValue(finalArguments, "--userType"), "legacy"));

        System.out.println("[Carbon Client] CarbonTweaker finalArgsCount=" + finalArguments.size());
        System.out.println("[Carbon Client] CarbonTweaker has --version=" + hasOption(finalArguments, "--version"));
        System.out.println("[Carbon Client] CarbonTweaker launch target args prepared.");

        return finalArguments.toArray(new String[finalArguments.size()]);
    }

    private File resolveGameDir() {
        if (gameDir != null) {
            return gameDir;
        }

        String userDir = System.getProperty("user.dir", ".");
        if (isBlank(userDir)) {
            return new File(".");
        }

        return new File(userDir);
    }

    private File resolveAssetsDir(File resolvedGameDir) {
        if (assetsDir != null) {
            return assetsDir;
        }

        return new File(resolvedGameDir, "assets");
    }

    private String resolveProfile() {
        return fallback(profile, DEFAULT_PROFILE);
    }

    private static boolean hasOption(List<String> args, String option) {
        return getOptionValue(args, option) != null;
    }

    private static String getOptionValue(List<String> args, String option) {
        if (args == null || option == null) {
            return null;
        }

        for (int index = 0; index < args.size(); index++) {
            String argument = args.get(index);
            if (option.equals(argument)) {
                if (index + 1 < args.size()) {
                    return args.get(index + 1);
                }

                return "";
            }
        }

        return null;
    }

    private static void putIfMissing(List<String> args, String option, String value) {
        if (hasOption(args, option)) {
            return;
        }

        args.add(option);
        args.add(fallback(value, ""));
    }

    private static String safePath(File file) {
        return file == null ? "null" : file.getAbsolutePath();
    }

    private static String safeValue(String value) {
        return value == null ? "null" : value;
    }

    private static String fallback(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
