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

## v0.5.3 Passive 1.8.9 Bridge Implementation

- Basic 1.8.9 `GameBridge`, `InputBridge`, and `RenderBridge` implementations were added.
- `V189BridgeBootstrap` registers the passive bridges through `BridgeRegistry`.
- Existing modules still use direct 1.8.9 code.
- `RenderUtils`, `CarbonMenuScreen`, `HudLayoutEditorScreen`, `ForgeEventBridge`, and HUD modules were not migrated.
- No runtime behavior migration yet.
- No 1.7.10 implementation yet.

## v0.5.4 Passive Bridge Runtime QA

- Passive bridges were checked for null safety and registry safety.
- `BridgeRegistry` ignores null registrations so existing bridges are not accidentally cleared.
- `V189InputBridge` safely handles uncreated LWJGL keyboard/mouse state.
- Bridge APIs remain inactive for existing modules.
- No module migration yet.
- No 1.7.10 implementation yet.
- Next safe step can be a low-risk bridge consumer prototype.

## v0.5.5 Bridge Diagnostics / Internal Validation

- Added internal `BridgeDiagnostics` helper.
- Added `BridgeDiagnosticsReport` data object.
- Passive Game/Input/Render bridge readiness can now be inspected safely.
- Existing modules still use the direct 1.8.9 path.
- No module migration yet.
- No 1.7.10 implementation yet.
- Diagnostics is internal only; no user-facing UI, HUD, command, keybind, or notification was added.

## v0.5.6 FPS Display Bridge-Assisted Render Prototype

- FPS Display is the first low-risk bridge-assisted consumer.
- `RenderBridge` may be used internally for FPS text, background rect, string width, and font height.
- Legacy render fallback remains mandatory and is still present.
- FPS settings, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.7 FPS Bridge Consumer QA

- FPS Display bridge-assisted render prototype was validated.
- Bridge draw failures now propagate to the FPS fallback guard so the legacy render path can recover.
- Legacy fallback remains mandatory.
- FPS options, config/profile format, and HUD Editor bounds remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.8 CPS Display Bridge-Assisted Render Prototype

- CPS Display is the second low-risk bridge-assisted consumer.
- `RenderBridge` may be used internally for CPS text, background rect, string width, and font height.
- Legacy render fallback remains mandatory and is still present.
- CPS counting logic remains unchanged and LMB-only.
- CPS settings, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.9 CPS Bridge Consumer QA

- CPS Display bridge-assisted render prototype was validated.
- Legacy fallback remains mandatory and still protects the HUD if the passive bridge is unavailable or returns invalid font metrics.
- CPS counting logic remains LMB-only; no right-click CPS, total CPS, or config/profile runtime counters were added.
- CPS options, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- FPS Display and CPS Display are the only bridge-assisted module consumers.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.10 Clock HUD Bridge-Assisted Render Prototype

- Clock HUD is the third low-risk bridge-assisted consumer.
- `RenderBridge` may be used internally for Clock HUD text, background rect, string width, and font height.
- Legacy render fallback remains mandatory and is still present.
- Clock local/system-time formatting logic remains unchanged.
- Show Seconds, 12H/24H format, Show Prefix, and Prefix Text behavior remain unchanged.
- Clock HUD settings, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.11 Clock HUD Bridge Consumer QA

- Clock HUD bridge-assisted render prototype was validated.
- Legacy fallback remains mandatory and still protects the HUD if the passive bridge is unavailable or returns invalid font metrics.
- Clock local/system-time formatting remains unchanged.
- Show Seconds, 12H/24H format, Show Prefix, and Prefix Text behavior remain unchanged.
- Clock HUD options, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- FPS Display, CPS Display, and Clock HUD are the only bridge-assisted module consumers.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.12 Low-Risk Bridge Consumers QA Pass

- FPS Display, CPS Display, and Clock HUD bridge-assisted consumers were validated together.
- Legacy fallback remains mandatory across all low-risk bridge consumers.
- `V189RenderBridge.drawText` now lets missing font renderer state reach the consumer fallback path instead of silently returning.
- Config/profile format and HUD Editor bounds remain unchanged.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.
- Next candidate remains Coordinates HUD, with caution because it reads player/world position, direction, and biome data.

## v0.5.13 Coordinates HUD Bridge-Assisted Render Prototype

- Coordinates HUD is the next bridge-assisted render consumer.
- Only the render path uses `RenderBridge`.
- Player, world, biome, coordinate, and direction data access remains legacy/direct 1.8.9.
- `EntityBridge` and `WorldBridge` are not used yet.
- Legacy render fallback remains mandatory and is still present.
- Coordinates HUD settings, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.14 Coordinates HUD Bridge Consumer QA

- Coordinates HUD bridge-assisted render prototype was validated.
- Legacy fallback remains mandatory.
- X/Y/Z, direction, and biome data access remain legacy/direct 1.8.9.
- `EntityBridge` and `WorldBridge` are still not used.
- Coordinates HUD options, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.

## v0.5.15 Low-Risk Render Bridge Consumers QA Pass II

- FPS Display, CPS Display, Clock HUD, and Coordinates HUD bridge-assisted consumers were validated together.
- Legacy fallback remains mandatory across all four consumers.
- Coordinates remains render-only bridge-assisted; player/world data is still direct 1.8.9.
- Config/profile format and HUD Editor bounds remain unchanged.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.
- Next candidate can be Keystrokes partial render, with caution due to pressed key, mouse, and space state handling.

## v0.5.16 Keystrokes Partial Render Bridge Prototype

- Keystrokes is now a partial bridge-assisted render consumer.
- Only the render path uses `RenderBridge`.
- W/A/S/D, LMB/RMB, and SPACE pressed state logic remains legacy/direct 1.8.9.
- `InputBridge` is not used yet.
- Legacy render fallback remains mandatory and is still present.
- Keystrokes settings, config/profile format, HUD Editor bounds, and visual behavior remain unchanged.
- No other modules were migrated.
- No 1.7.10 implementation yet.

## v0.5.17 Keystrokes Bridge Consumer QA

- Keystrokes partial render bridge prototype was validated.
- Legacy fallback remains mandatory.
- Bridge metric failures now stay inside the Keystrokes fallback guard and return to the legacy render path.
- W/A/S/D, LMB/RMB, and SPACE pressed state remains legacy/direct 1.8.9.
- `InputBridge` is still not used.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.

## v0.5.18 Render Bridge Consumers QA Pass III

- FPS Display, CPS Display, Clock HUD, Coordinates HUD, and Keystrokes bridge-assisted consumers were validated together.
- Legacy fallback remains mandatory across all five consumers.
- Coordinates remains render-only bridge-assisted; player/world data is still direct 1.8.9.
- Keystrokes remains render-only bridge-assisted; key/mouse state is still direct 1.8.9.
- Config/profile format and HUD Editor bounds remain unchanged.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.
- Next possible step can be render bridge helper cleanup/extraction if duplication is becoming too high.

## v0.5.19 Render Bridge Helper Cleanup / Extraction

- `RenderBridge` access and fallback preparation was centralized into a small helper.
- Existing bridge consumers remain FPS Display, CPS Display, Clock HUD, Coordinates HUD, and Keystrokes.
- Existing consumers still own module-specific render layout, drawing decisions, and legacy fallback.
- Coordinates remains render-only bridge-assisted with direct 1.8.9 data access.
- Keystrokes remains render-only bridge-assisted with direct legacy input state.
- No `InputBridge`, `EntityBridge`, or `WorldBridge` usage was introduced.
- No additional module migration was introduced.
- No 1.7.10 implementation yet.

## v0.5.20 Render Bridge Helper QA

- `RenderBridgeAccess` helper was validated.
- Existing bridge consumers remain FPS Display, CPS Display, Clock HUD, Coordinates HUD, and Keystrokes.
- No new module migration was introduced.
- Coordinates remains render-only bridge-assisted with direct 1.8.9 data access.
- Keystrokes remains render-only bridge-assisted with direct legacy input state.
- No `InputBridge`, `EntityBridge`, or `WorldBridge` usage was introduced.
- No 1.7.10 implementation yet.
- Next possible step: Ping Display render-only bridge prototype.

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
- v0.5.3: Passive 1.8.9 Game/Input/Render bridge implementation.
- v0.5.4: Passive bridge runtime QA and null-safety hardening.
- v0.5.5: Internal bridge diagnostics and readiness validation.
- v0.5.6: FPS Display bridge-assisted render prototype with legacy fallback.
- v0.5.7: FPS bridge consumer QA and fallback validation.
- v0.5.8: CPS Display bridge-assisted render prototype with legacy fallback.
- v0.5.9: CPS bridge consumer QA with LMB-only counting and fallback validation.
- v0.5.10: Clock HUD bridge-assisted render prototype with legacy fallback.
- v0.5.11: Clock HUD bridge consumer QA with local/system-time formatting and fallback validation.
- v0.5.12: Low-risk bridge consumers QA pass for FPS, CPS, and Clock HUD.
- v0.5.13: Coordinates HUD bridge-assisted render prototype, render-only bridge use.
- v0.5.14: Coordinates HUD bridge consumer QA, render-only bridge use validated.
- v0.5.15: Low-risk render bridge consumers QA pass II for FPS, CPS, Clock, and Coordinates.
- v0.5.16: Keystrokes partial render bridge prototype with key/mouse state still direct 1.8.9.
- v0.5.17: Keystrokes bridge consumer QA with InputBridge still unused.
- v0.5.18: Render bridge consumers QA pass III for FPS, CPS, Clock, Coordinates, and Keystrokes.
- v0.5.19: Render bridge helper cleanup/extraction for current render consumers.
- v0.5.20: Render bridge helper QA for existing render consumers.
- v0.5.21: Ping Display render-only bridge prototype or careful Armor/Potion risk analysis.
- Later: create the separate 1.7.10 environment only after 1.8.9 remains stable through bridge proof of concept.
