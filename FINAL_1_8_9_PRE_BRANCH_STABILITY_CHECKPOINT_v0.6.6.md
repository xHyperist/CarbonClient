# Final 1.8.9 Pre-Branch Stability Checkpoint v0.6.6

## 1. Purpose

This document is the final stability checkpoint for the current Minecraft Forge 1.8.9 runtime before the user manually creates an isolated 1.7.10 experimental branch.

v0.6.6 is a checkpoint-only release. It does not create a branch, does not start the 1.7.10 port, does not add dependencies, does not change `build.gradle`, and does not change runtime behavior.

## 2. Current Runtime

- Current runtime: Minecraft Forge 1.8.9.
- Current version: 0.6.6.
- Current main branch should remain stable 1.8.9.
- Future experimental branch name remains `experimental/1.7.10-runtime`.
- 1.7.10 support is not active yet.

Current render-only bridge-assisted consumers:

- FPS Display.
- CPS Display.
- Clock HUD.
- Coordinates HUD.
- Keystrokes.
- Ping Display.

## 3. Clean Build Checklist

- [ ] Confirm working tree is intentionally clean or intentionally staged by the user.
- [ ] Confirm `build.gradle` has no 1.7.10 dependency.
- [ ] Confirm `gradle.properties` still targets `minecraft_version=1.8.9`.
- [ ] Run `.\gradlew.bat clean build`.
- [ ] Confirm `BUILD SUCCESSFUL`.
- [ ] Confirm `build/libs/carbon-client-0.6.6.jar` is produced.

## 4. runClient Manual QA Checklist

- [ ] Run `.\gradlew.bat runClient`.
- [ ] Minecraft launches without startup crash.
- [ ] Carbon Client init logs appear without repeated error spam.
- [ ] Main menu loads normally.
- [ ] Enter a world or test server without Carbon-related crash.
- [ ] Close runClient and confirm no config save error is shown.

## 5. Config/Profile Persistence Manual QA Checklist

- [ ] Toggle FPS Display on/off, restart, confirm enabled state persists.
- [ ] Toggle CPS Display on/off, restart, confirm enabled state persists.
- [ ] Toggle Clock HUD on/off, restart, confirm enabled state persists.
- [ ] Toggle Coordinates HUD on/off, restart, confirm enabled state persists.
- [ ] Toggle Keystrokes on/off, restart, confirm enabled state persists.
- [ ] Toggle Ping Display on/off, restart, confirm enabled state persists.
- [ ] Toggle at least one visual module, restart, confirm enabled state persists.
- [ ] Switch profile if available and confirm profile-specific enabled states apply.
- [ ] Confirm runtime-only values are not persisted as config/profile settings.

## 6. HUD Modules Manual QA Checklist

- [ ] FPS Display renders and can be toggled.
- [ ] CPS Display renders and can be toggled.
- [ ] Clock HUD renders and can be toggled.
- [ ] Coordinates HUD renders and can be toggled.
- [ ] Keystrokes renders and can be toggled.
- [ ] Ping Display renders and can be toggled.
- [ ] Reach Display renders without crash when enabled.
- [ ] Combo Display renders without crash when enabled.
- [ ] Armor HUD renders without crash when enabled.
- [ ] Potion HUD renders without crash when enabled.
- [ ] ToggleSprint / ToggleSneak behavior remains unchanged.
- [ ] Crosshair editor/preview still opens without crash.

## 7. HUD Editor Manual QA Checklist

- [ ] Open Carbon Menu with RSHIFT.
- [ ] Open HUD Editor.
- [ ] Move FPS Display, save/exit/re-enter, confirm position persists.
- [ ] Move Ping Display, save/exit/re-enter, confirm position persists.
- [ ] Move Keystrokes, save/exit/re-enter, confirm position persists.
- [ ] Move Clock HUD, save/exit/re-enter, confirm position persists.
- [ ] Move Coordinates HUD, save/exit/re-enter, confirm position persists.
- [ ] Restart client and confirm edited HUD positions persist.

## 8. Visual Modules Manual QA Checklist

- [ ] Fullbright toggles on.
- [ ] Fullbright toggles off.
- [ ] Fullbright gamma/original brightness restore works.
- [ ] Time Changer toggles on.
- [ ] Time Changer visual world time effect is stable.
- [ ] Time Changer toggles off without world/client crash.
- [ ] Visuals tab remains responsive.

## 9. Carbon Menu / Options Manual QA Checklist

- [ ] RSHIFT opens Carbon Menu.
- [ ] Mods tab search works.
- [ ] Mods tab category filters work.
- [ ] Mods tab scroll works.
- [ ] Options screen opens.
- [ ] Module options screens open.
- [ ] Boolean settings save after change.
- [ ] Number sliders save after change.
- [ ] Mode settings save after change.
- [ ] Color picker opens and saves selected color.
- [ ] Keybinds screen opens and conflict warnings still work.
- [ ] Profiles screen opens without crash.

## 10. PvP / Stability Manual QA Checklist

- [ ] Reach Display enabled does not crash.
- [ ] Combo Display enabled does not crash.
- [ ] CPS Display remains lightweight during clicking.
- [ ] Keystrokes pressed states update without visible input delay.
- [ ] Ping Display renders without network/player info crash.
- [ ] No packet spam, rod exploit, autoclicker, reach/hitbox/velocity/aim assist behavior exists.
- [ ] No repeated log spam appears during normal play.

## 11. Known Deferred Systems

These systems are intentionally deferred from the first 1.7.10 experiment:

- Armor HUD item render migration.
- Potion HUD icon/texture migration.
- ToggleSprint movement/input migration.
- Crosshair overlay migration.
- Reach Display entity/raycast/event migration.
- Combo Display attack/damage/entity migration.
- Fullbright game settings abstraction.
- Time Changer world time abstraction.
- Carbon Menu / HUD Editor GUI migration.
- RenderUtils full bridge migration.
- InputBridge, EntityBridge, WorldBridge, NetworkBridge active usage.

## 12. Before Opening the Branch

- [ ] Main 1.8.9 clean build is successful.
- [ ] Any uncommitted main changes are intentionally committed, stashed, or documented by the user.
- [ ] v0.6.6 checkpoint document is reviewed.
- [ ] Manual runClient QA is completed or explicitly deferred with notes.
- [ ] The user manually creates `experimental/1.7.10-runtime`.
- [ ] First experiment remains minimal Forge 1.7.10 bootstrap only.
- [ ] No HUD/module port is attempted in the first experiment.
- [ ] Rollback path is understood.

## 13. What We Are Not Doing Yet

- Not creating a branch.
- Not starting the 1.7.10 port.
- Not adding 1.7.10 dependencies.
- Not changing `build.gradle`.
- Not moving packages.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not changing runtime behavior.
- Not performing a large refactor.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
