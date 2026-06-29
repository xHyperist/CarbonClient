package com.carbonclient.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public final class CarbonTweaker implements ITweaker {

    private static final String LAUNCH_TARGET = "net.minecraft.client.main.Main";

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
        return launchArguments.toArray(new String[launchArguments.size()]);
    }

    private static String safePath(File file) {
        return file == null ? "null" : file.getAbsolutePath();
    }

    private static String safeValue(String value) {
        return value == null ? "null" : value;
    }
}
