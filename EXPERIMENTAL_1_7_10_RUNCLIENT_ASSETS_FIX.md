# Experimental 1.7.10 runClient Assets Fix

## Purpose

This note documents the `runClient` assets index workaround for the `experimental/1.7.10-runtime` branch.

The fix is experimental-branch only. It does not port HUD modules, does not change runtime behavior, and does not add any gameplay feature.

## Problem

ForgeGradle 1.2 still knows some legacy Mojang S3 URLs for Minecraft 1.7.10 assets.

During `.\gradlew.bat runClient`, the `getAssetsIndex` step can fail when the expected local file is missing:

```text
getAssetsIndex FAILED
FileNotFoundException:
C:\Users\cihan\.gradle\caches\minecraft\assets\indexes\1.7.10.json
```

The normal `clean build` path can pass while `runClient` still fails because the client run task needs the assets index.

## Workaround

`build.gradle` now defines `prepareLegacyAssetsIndex`.

The task:

1. Checks whether Gradle's expected assets index already exists:
   `~/.gradle/caches/minecraft/assets/indexes/1.7.10.json`
2. If missing, tries to copy it from the local Minecraft launcher cache:
   `%APPDATA%/.minecraft/assets/indexes/1.7.10.json`
3. If the local launcher cache does not have it, downloads the modern Mojang version manifest and follows the official 1.7.10 asset index URL.

`getAssetsIndex` and `runClient` depend on this task so the file is prepared before ForgeGradle tries to use it.

## Remaining Risk

ForgeGradle 1.2 may still print legacy URL warnings such as:

```text
http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/1.7.10.json  404'ed!
```

That warning is from old ForgeGradle internals. The goal of this workaround is to make the required local assets index available so minimal `runClient` can proceed.

## Boundaries

- No HUD/module port.
- No config/profile migration.
- No main-branch 1.8.9 change.
- No cheat/PvP advantage feature.
- No large refactor.
