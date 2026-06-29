package com.carbonclient.launch;

public final class CarbonBootstrap {

    private static boolean launcherMode;
    private static boolean bootstrapped;

    private CarbonBootstrap() {
    }

    public static boolean isLauncherMode() {
        return launcherMode;
    }

    public static boolean isBootstrapped() {
        return bootstrapped;
    }

    public static void markLauncherMode() {
        launcherMode = true;
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }

        bootstrapped = true;
        System.out.println("[Carbon Client] Launcher tweaker bootstrap initialized.");
    }
}
