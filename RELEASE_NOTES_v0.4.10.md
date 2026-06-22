# Carbon Client v0.4.10 Release Notes

## Purpose

Carbon Client v0.4.10 is the final Minecraft 1.8.9 Release Candidate QA build before multi-version architecture planning begins.

This release does not add new gameplay features or new modules. It focuses on stabilization, documentation cleanup, config/profile compatibility, and release readiness for the existing 1.8.9 client core.

## Highlighted Systems

- Carbon Menu with Mods, Visuals, Settings, Profiles, and HUD Editor tabs
- Mods Tab search, category filters, and scrollable grid
- Options Screen with Setting system integration
- Keybinds Panel with conflict warnings
- Config save/load, defaults, and reset flows
- Profiles create, duplicate, load, save, rename, and delete flows
- Notification / Toast system
- Color Picker
- HUD Layout Editor
- Visuals: Fullbright and Time Changer
- HUD modules: FPS, CPS, Keystrokes, ToggleSprint, Armor, Potion, Coordinates, Ping, Reach, Combo, Clock
- Crosshair Editor with preview

## Safety Notes

Carbon Client is not a cheat client. This release does not add fly, speed, reach, velocity, autoclicker, aim assist, aura, triggerbot, hitbox changes, packet manipulation, or attack/damage cancellation behavior.

Reach Display and Combo Display remain informational HUD modules only.

## Later / Research Required

- Scoreboard customization after safe vanilla render hook research
- Zoom after OptiFine and launcher compatibility planning
- Block Overlay
- Chat Mod
- Weather Changer

## Multi-Version Plan

The 1.7.10 port has not started in this release.

Next planned direction:

- v0.5.0 Multi-Version Architecture Planning
- 1.7.10 port preparation
- Later launcher version selector work

## Manual QA Recommendation

Before treating this as a release candidate build, test:

- `.\gradlew.bat clean build`
- `.\gradlew.bat runClient`
- RSHIFT Carbon Menu
- Mods Tab search/category/scroll
- Options, Keybinds, Profiles, Visuals, Color Picker, and HUD Editor
- Config/profile restart persistence
- Singleplayer and multiplayer startup
