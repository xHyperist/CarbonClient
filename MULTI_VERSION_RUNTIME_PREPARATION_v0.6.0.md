# Multi-Version Runtime Preparation v0.6.0

## 1. Purpose

Carbon Client v0.6.0 starts the multi-version runtime preparation phase after the v0.5.x bridge preparation work.

This is a planning and strategy checkpoint. It does not start the 1.7.10 port, does not change `build.gradle`, does not add source sets, does not add a new runtime, and does not add a new bridge consumer.

The purpose is to define how Carbon Client can move toward Minecraft 1.7.10 support later while preserving the current stable 1.8.9 runtime.

## 2. Current State from v0.5.x

Completed in v0.5.x:

- Bridge API skeleton.
- `BridgeRegistry`.
- `BridgeDiagnostics`.
- Passive 1.8.9 bridge implementation.
- `RenderBridgeAccess` helper.
- Render-only bridge consumers:
  - FPS Display.
  - CPS Display.
  - Clock HUD.
  - Coordinates HUD.
  - Keystrokes.
  - Ping Display.
- Module enabled state persistence hotfix.
- Armor/Potion bridge risk analysis.
- Multi-Version Bridge Phase Review.

Not started yet:

- 1.7.10 runtime.
- `build.gradle` multi-version setup.
- Source set separation.
- InputBridge usage.
- EntityBridge usage.
- WorldBridge usage.
- NetworkBridge.
- Launcher/auth/cosmetic systems.

## 3. Main Goals for v0.6.x

Carbon Client long-term goals:

- Professional Minecraft client experience.
- Multi-version support, initially 1.8.9 then 1.7.10.
- High FPS / performance-first architecture.
- Low input delay.
- Smooth PvP responsiveness.
- Rod responsiveness: fishing rod usage should feel instant and smooth on the client side.
- Clean HUD/UI system.
- Stable config/profile system.
- Future launcher with Premium Minecraft account login and Guest Mode.
- Future cosmetics/capes/cloud config/permission system.

Carbon Client must not become a cheat client.

Forbidden:

- Reach increase.
- Hitbox manipulation.
- Velocity manipulation.
- Aim assist.
- Autoclicker.
- Packet manipulation.
- Rod cooldown bypass.
- Fishing rod exploit.
- Server-side cooldown bypass.
- Attack packet spam.
- Fly, speed, or killaura.
- Any PvP advantage that changes server-side mechanics.

Legit goals:

- FPS stability.
- Frame time stability.
- Reduced input latency.
- Reduced render tick overhead.
- Reduced unnecessary allocation.
- Ping/jitter awareness.
- Avoiding client slowdowns during PvP.
- Smooth client-side feel for rod, block hit, bow, pearl, and item-use interactions.

## 4. Performance-First Architecture Rule

Carbon Client must avoid unnecessary per-frame overhead.

Rules:

- Do not allocate unnecessary objects in render tick paths.
- Do not save config/profile data during render tick.
- Do not perform disk IO every frame.
- Do not log every frame.
- Keep HUD render code lightweight.
- Keep bridge abstractions thin enough that they do not reduce FPS.
- Calculate runtime values only when needed.
- Cache or lazily compute expensive values when appropriate.
- Keep string formatting lightweight.
- Disabled modules should not do unnecessary tick/render work.
- Save config/profile data only when state changes.
- Minimize and control GL state changes.
- Design UI animations so they do not harm frame timing.

This rule applies to future v0.6, v0.7, and v0.8 work.

## 5. PvP Responsiveness / Rod Responsiveness Rule

Carbon Client should improve PvP feel through FPS stability, input responsiveness, lightweight rendering, and stable frame timing, not through cheat mechanics.

Specific expectations:

- Fishing rod usage should feel low-delay.
- Right click and item use should not be delayed by Carbon systems.
- HUD rendering should not cause frame spikes during PvP.
- Config/profile saving should never happen continuously during combat.
- Logs/debug output should not spam during PvP.
- Input state should remain clean and stable.
- Keystrokes and CPS systems should stay lightweight.
- Ping, FPS, and frame-time visibility can help users understand PvP feel.

Explicitly not allowed:

- Rod cooldown bypass.
- Packet-level rod exploit.
- Automatic rod usage.
- Packet spam.
- Reach or hit manipulation.
- Server-side mechanic manipulation.

## 6. Common vs Version-Specific Strategy

The goal is to separate common behavior from version-specific runtime access without breaking the working 1.8.9 client.

Likely common systems should stay free from direct Minecraft/Forge/LWJGL dependencies where possible.

Version-specific systems should remain isolated behind bridge/adapters before 1.7.10 work begins.

Mixed systems should not be moved in one large refactor. They need careful adapter plans.

## 7. Candidate Common Packages

Likely common:

- Module base abstraction, with care around keybind naming and lifecycle hooks.
- Settings system.
- Boolean, Number, Mode, and Color settings.
- Config/Profile serialization, excluding runtime-only values.
- Theme tokens and design constants.
- Notification model.
- `RenderBridge` API.
- `BridgeRegistry`.
- `BridgeDiagnostics`.
- `RenderBridgeAccess`.
- Documentation and roadmap.

## 8. Candidate Version-Specific Packages

Likely version-specific:

- Forge event hooks.
- `KeyInputHandler`.
- Direct Minecraft/Forge imports.
- Entity/player/world access.
- Network/player info ping access.
- ItemStack render.
- Potion icon render.
- Fullbright gamma access.
- Time Changer world time access.
- Crosshair overlay render.
- Reach/Combo entity event logic.
- `RenderUtils` low-level GL calls.
- GUI screen implementation if Forge/Minecraft screen APIs differ.

Mixed / needs adapter:

- HUD modules.
- `DraggableHudModule`.
- `CarbonMenuScreen`.
- `HudLayoutEditorScreen`.
- Input state.
- Render text/rect helpers.
- Profile module snapshots.
- Keybinds.

## 9. Build Strategy Options

### Option A - Separate Branches

Examples:

- `main-1.8.9`
- `experimental-1.7.10`

Pros:

- Safer.
- Less `build.gradle` risk.
- Easier rollback.

Cons:

- Code duplication risk.
- Shared logic improvements need careful cherry-picking or merge discipline.

### Option B - Source Sets

Examples:

- `src/common/java`
- `src/v1_8_9/java`
- `src/v1_7_10/java`

Pros:

- Cleaner architecture.
- Shared code is easier to reason about.

Cons:

- Old ForgeGradle risk.
- More build complexity.
- Higher chance of breaking the stable 1.8.9 setup if attempted too early.

### Option C - Multi-Project Gradle

Examples:

- `carbon-common`
- `carbon-1.8.9`
- `carbon-1.7.10`

Pros:

- Professional long-term structure.
- Strong version separation.

Cons:

- Higher initial risk.
- Old ForgeGradle compatibility issues.
- Bigger migration cost.

## 10. Recommended Strategy

For now, start v0.6.x planning with Option A or a cautious Option B.

Do not change `build.gradle` in v0.6.0.

For the first 1.7.10 experiment, prefer an isolated branch so the stable 1.8.9 runtime is not broken.

Only move toward source sets or multi-project Gradle after the 1.7.10 environment strategy is proven.

## 11. Risks

- ForgeGradle and MCP mapping differences between 1.8.9 and 1.7.10.
- Forge event name/timing differences.
- GUI and render API differences.
- Keyboard/mouse input differences.
- Entity/world/player API differences.
- Network/player info ping differences.
- ItemStack and potion API differences.
- GL state assumptions in item/icon rendering.
- Config/profile compatibility if module names or settings diverge.
- Performance regressions caused by too much bridge abstraction.
- Accidental runtime value persistence.
- PvP feel regressions from heavy HUD/UI work.

## 12. v0.6.x Roadmap

| Version | Goal |
| --- | --- |
| v0.6.0 | Multi-Version Runtime Preparation; performance/PvP responsiveness goals documented. |
| v0.6.1 | 1.7.10 Environment Strategy; branch/source-set decision. |
| v0.6.2 | Common vs Version-Specific Package Separation Plan; no runtime migration yet. |
| v0.6.3 | Isolated 1.7.10 Experimental Branch Preparation; no main branch breakage. |
| v0.6.4 | Low-risk module port candidate analysis: Clock/FPS/CPS/Keystrokes/Ping. |
| v0.6.5 | First isolated 1.7.10 runtime experiment, if safe. |

Every v0.6.x step must preserve high-FPS architecture and avoid unnecessary overhead.

Future v0.8.x - Performance / FPS Optimization Phase:

- HUD Render Performance Pass.
- Config/Profile IO Optimization.
- Render tick allocation reduction.
- GL state optimization.
- Module tick optimization.
- Frame time monitoring.
- PvP Low Latency Mode planning.
- Rod responsiveness validation.
- Benchmark / FPS test checklist.

The v0.8.x list is a roadmap note only. It is not implemented in v0.6.0.

## 13. Entry Criteria for First 1.7.10 Experiment

Before the first 1.7.10 experiment:

- 1.8.9 clean build must pass.
- v0.5.x render-only bridge consumers should remain stable.
- Module enabled-state persistence should be manually restart-tested if still unverified.
- Branch/source strategy must be chosen.
- 1.7.10 ForgeGradle/MCP constraints must be documented.
- Common vs version-specific package plan must be documented.
- Low-risk module candidates must be chosen.
- No combat advantage or server-side mechanic change may be introduced.

## 14. What We Are Not Doing Yet

- Not starting the 1.7.10 port.
- Not changing `build.gradle` for multi-version runtime.
- Not adding source sets.
- Not adding a multi-project structure.
- Not adding a new Forge dependency.
- Not adding a new Minecraft runtime.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not moving Armor/Potion to bridge.
- Not using InputBridge, EntityBridge, WorldBridge, or NetworkBridge.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
