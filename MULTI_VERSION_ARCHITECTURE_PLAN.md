# Carbon Client Multi-Version Architecture Plan

## 1. Purpose

Carbon Client v0.5.0 starts planning for a future multi-version client that can support Minecraft 1.8.9 first and Minecraft 1.7.10 later.

This document is intentionally planning-only. It does not start the 1.7.10 port, does not convert Gradle into a multi-project build, and does not move the working 1.8.9 code.

## v0.5.1 Safe Bridge Preparation

- Bridge package skeleton added under `com.carbonclient.bridge`.
- Interfaces added for future Game, Render, Input, Event, Entity, World, and Config abstraction.
- `BridgeRegistry` added as a future coordination point; the current runtime does not depend on it.
- `BridgeVersionInfo` marks the future 1.8.9 bridge implementation as `PLANNED`.
- Existing 1.8.9 runtime still uses the current direct implementation.
- No 1.7.10 implementation yet.
- No `build.gradle` or source-set changes.
- No module migration yet.

## v0.5.2 Bridge Dependency Mapping

- Added `BRIDGE_DEPENDENCY_MAP_v0.5.2.md`.
- Current 1.8.9 Minecraft/Forge dependencies are classified as common-ready, common with minor adapter, version-specific, or risky to port.
- Modules are mapped to future Game, Render, Input, Event, Entity, World, and Config bridge needs.
- No runtime migration yet.
- No 1.7.10 implementation yet.
- Bridge API remains an inactive skeleton.
- Next safe step should be low-risk bridge implementation, starting with Game/Input/Render basics.

## 2. Current 1.8.9 Status

The 1.8.9 Forge client has a stable Release Candidate baseline:

- Core lifecycle through `Client.java` and `CarbonClient.java`
- Module system, EventBus, settings, config, profiles, notifications
- Carbon Menu, Mods Tab, Options, Keybinds, Profiles, Visuals, Color Picker, HUD Editor
- HUD modules: FPS, CPS, Keystrokes, ToggleSprint, Armor, Potion, Coordinates, Ping, Reach, Combo, Clock
- Crosshair Editor
- Visuals: Fullbright and Time Changer

The current implementation directly imports Minecraft 1.8.9 and Forge classes in many render, input, event, world, and entity paths. That is acceptable for the current 1.8.9 build, but it should be isolated before adding 1.7.10.

## 3. Why Multi-Version Architecture Is Needed

Minecraft 1.8.9 and 1.7.10 have similar client concepts but different mappings, Forge events, render APIs, entity APIs, potion APIs, GUI behavior, and build environments.

Without a compatibility layer, porting would require copying or rewriting modules per version. The goal is to keep business logic, module metadata, settings, config, profiles, notifications, and UI concepts common while moving direct Minecraft/Forge calls behind version-specific bridge/adapters.

## 4. Common Systems

These systems can remain mostly common:

- `setting/`: `Setting`, `BooleanSetting`, `NumberSetting`, `ModeSetting`, `ColorSetting`, `KeybindSetting`
- `module/`: module metadata, enable/disable lifecycle, categories, keybind values
- `config/`: JSON snapshot format, defaults, reset behavior, unknown old field tolerance
- `profile/`: profile storage, active profile metadata, snapshot application
- `notification/`: notification model, type, manager, render concept
- `ui/theme/`: Carbon design tokens
- `ui/component/`: button, card, slider, toggle, color picker concepts
- General HUD position semantics through `DraggableHudModule`
- Roadmap/auth/launcher policy documents
- Account, permission, cosmetic, cloud, launcher, update service skeleton concepts

Common code should avoid direct `net.minecraft.*` and `net.minecraftforge.*` imports when practical.

## 5. Version-Specific Systems

These systems are likely version-specific:

- `ForgeEventBridge`: Forge event classes, event names, cancellation behavior
- Render calls: `Gui.drawRect`, item renderer, GL state assumptions, font renderer access
- GUI classes: `GuiScreen`, scaled resolution, mouse wheel behavior
- Input events: Forge key/mouse event classes and LWJGL state reads
- Entity/world/player access: `thePlayer`, `theWorld`, bounding boxes, eye height, hurt state
- Potion/effect APIs: active effects, names, durations, icons
- Ping APIs: server data/player info availability
- Scoreboard/render hook research
- Crosshair vanilla overlay hook
- Fullbright gamma access and restore behavior
- Time Changer world time access
- Reach target bounding box access
- Combo damage/hurt event access

## 6. Proposed Long-Term Package Structure

Do not move the current code immediately. Long term, the structure can become:

```text
src/
  common/
    core/
    module/
    setting/
    config/
    profile/
    notification/
    ui/
    hud/
    service/
    version/
  v1_8_9/
    bridge/
    render/
    input/
    world/
    entity/
    moduleimpl/
  v1_7_10/
    bridge/
    render/
    input/
    world/
    entity/
    moduleimpl/
```

The current source tree should stay stable until bridge boundaries are proven with small adapters.

## 7. Compatibility Layer Proposal

Planning interfaces:

### GameBridge

- `getPlayer()`
- `getWorld()`
- `getDisplayWidth()`
- `getDisplayHeight()`
- `isInGame()`

### RenderBridge

- `drawRect`
- `drawText`
- `getStringWidth`
- `renderItem`
- `setupScale`
- GL state reset helpers

### InputBridge

- key state
- mouse state
- keybind registration/handling
- screen-focused input checks

### EventBridge

- render2D
- key input
- mouse input
- attack entity
- player damage
- world load/unload
- client tick
- crosshair render/hide hook

### EntityBridge

- player position
- eye height
- bounding box
- hurt time / damage animation state
- display name/team formatting where needed
- response time/ping if available

### WorldBridge

- world time
- biome access
- dimension/server state
- null-safe world/player checks

### ConfigBridge

- optional version-specific migration notes
- snapshot compatibility when module names or settings differ between versions

## 8. Module Portability Matrix

| Module/System | Port Difficulty | Why |
| --- | --- | --- |
| Clock HUD | Easy | Uses Java local time and simple HUD rendering. Needs only font/render bridge. |
| FPS Display | Easy | FPS value and text drawing may differ slightly but logic is simple. |
| CPS Display | Easy | Mouse click capture differs by Forge input event, but CPS logic is common. |
| Keystrokes | Easy | Key/mouse state should move behind InputBridge. |
| Coordinates HUD | Easy | Player position/biome reads need WorldBridge but logic is straightforward. |
| Armor HUD | Medium | ItemStack durability and item renderer differ across versions. |
| Potion HUD | Medium | Potion/effect icon/name/duration APIs may differ. |
| Ping Display | Medium | Multiplayer ping data access differs and may be limited in 1.7.10. |
| ToggleSprint | Medium | KeyBinding internals and sneak/sprint behavior differ across versions. |
| Fullbright | Medium | Gamma setting access and restore behavior should be bridged. |
| Time Changer | Medium | World time manipulation is risky and version-sensitive. |
| Crosshair | Risky | Vanilla crosshair event/hide hook differs by Forge version. |
| Reach Display | Risky | Bounding box/hit event mappings differ; must remain informational only. |
| Combo Display | Risky | Attack and hurt event timing differs; duplicate guard must be revalidated. |
| Scoreboard future | Risky | Vanilla sidebar render hook needs research per version. |
| Zoom future | Risky | Should wait for OptiFine/launcher compatibility planning. |
| Block Overlay future | Risky | Render hook and block outline pipeline differ. |
| Chat Mod future | Risky | Chat GUI and message pipeline differ. |

## 9. 1.7.10 Port Risks

- ForgeGradle and MCP mappings differ from the current 1.8.9 setup.
- GUI/render classes may have different method names or event timing.
- Overlay render events may not match 1.8.9 behavior.
- Item, potion, ping, scoreboard, and team formatting APIs may differ.
- Sprint/sneak and keybind behavior may need separate handling.
- PvP info HUD modules must stay informational and must not change combat behavior.
- Time Changer must not persist unwanted world time changes.
- Config/profile snapshots must ignore unsupported modules/settings safely.

## 10. Launcher/Auth/Guest Mode Compatibility Notes

- Carbon Client will not create its own in-client register/account system.
- Future authentication belongs in the launcher.
- Premium Minecraft Account connection is planned.
- Guest Mode is planned.
- Premium account connection should follow official Microsoft/Mojang-compatible account flow concepts.
- Rank/Permission should come after launcher/auth foundations.
- Cosmetics/Cape should come after rank/permission foundations.
- v0.5.0 does not implement launcher, auth, permission, rank, cosmetic, cape, cloud, or account code.

## 11. Migration Phases

### Phase 1 - Planning

- Current v0.5.0 task.
- No major refactor.
- Keep 1.8.9 build stable.

### Phase 2 - Safe Abstraction

- Identify bridge boundaries.
- Add small adapters only where they reduce direct Minecraft/Forge coupling.
- Keep behavior unchanged.

### Phase 3 - Split Version-Specific Calls

- Move Forge/Minecraft direct calls behind bridge implementations.
- Keep module logic and settings common where possible.
- Continue building 1.8.9 after every small step.

### Phase 4 - 1.7.10 Environment

- Add a separate 1.7.10 workspace/project after bridge seams are proven.
- Do not break the 1.8.9 build.

### Phase 5 - Feature Parity

- Port safe HUD modules first: Clock, FPS, CPS, Keystrokes, Coordinates.
- Port medium modules second.
- Delay risky modules until render/event hooks are proven.

### Phase 6 - Launcher / Version Selector

- Add launcher/version selector only after both versions boot reliably.
- Add Premium account connection and Guest Mode planning around the launcher.

## 12. Rules Before Starting 1.7.10 Port

- Do not convert Gradle structure until the bridge plan is validated.
- Do not move working 1.8.9 modules without a passing build after each step.
- Do not copy risky render hooks blindly between versions.
- Do not reintroduce Scoreboard, Zoom, Block Overlay, Chat Mod, Weather Changer, or Capture as active features during bridge work.
- Keep config/profile unknown-field tolerance.
- Keep PvP HUD modules informational only.

## 13. v0.5.x Roadmap

- v0.5.0: Multi-version architecture planning.
- v0.5.1: Safe bridge API skeleton and registry preparation.
- v0.5.2: Bridge dependency mapping and migration order documentation.
- v0.5.3: Add low-risk inactive 1.8.9 bridge implementations if needed.
- v0.5.4: Move one low-risk HUD render path behind a bridge as a proof of concept.
- v0.5.5: Validate config/profile compatibility for version-specific modules.
- Later: create the separate 1.7.10 environment only after 1.8.9 remains stable through bridge proof of concept.
