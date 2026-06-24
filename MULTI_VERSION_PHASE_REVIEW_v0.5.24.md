# Multi-Version Phase Review v0.5.24

## 1. Purpose

This document is a v0.5.x checkpoint for Carbon Client's multi-version bridge preparation.

The goal is to review the current bridge architecture, classify existing render-only bridge consumers, document systems that remain legacy/direct 1.8.9, and prepare a controlled v0.6.0 entry plan.

This review does not add a new module, does not add a new bridge consumer, does not start the 1.7.10 port, and does not change runtime behavior.

## 2. Current Bridge Architecture

- `com.carbonclient.bridge.api.*`: common bridge interfaces for game, render, input, event, entity, world, and config concepts.
- `com.carbonclient.bridge.registry.*`: central optional bridge registry. It is null-safe and remains a coordination point, not a runtime migration trigger by itself.
- `com.carbonclient.bridge.diagnostics.*`: crash-safe bridge readiness diagnostics used internally.
- `com.carbonclient.bridge.render.*`: small helper layer for safe `RenderBridge` readiness and font metric access.
- `com.carbonclient.bridge.impl.v1_8_9.*`: passive 1.8.9 bridge implementations.

Boundary review:

- `bridge.api`, `bridge.registry`, `bridge.diagnostics`, and `bridge.render` must stay free of `net.minecraft`, `net.minecraftforge`, and `org.lwjgl` imports.
- Minecraft/Forge/LWJGL usage remains acceptable in `impl.v1_8_9` and existing legacy 1.8.9 modules.
- `RenderBridgeAccess` stays small and module-agnostic. It only helps with bridge readiness and safe metrics.
- Each consumer still owns its own layout, drawing order, and legacy fallback decision.
- `V189RenderBridge` must not hide drawing or metric failures in a way that prevents consumer fallback.

## 3. Current Render-Only Bridge Consumers

| Module | Bridge usage | Data/input source | Fallback | Portability risk | Notes |
| --- | --- | --- | --- | --- | --- |
| FPS Display | Render-only | Minecraft FPS / existing 1.8.9 path | Legacy render path | Low | Good low-risk bridge consumer. |
| CPS Display | Render-only | Legacy left mouse click tracking | Legacy render path | Low-Medium | Counting remains LMB-only; no InputBridge. |
| Clock HUD | Render-only | Java/system local time | Legacy render path | Low | No Minecraft world time dependency. |
| Coordinates HUD | Render-only | Direct 1.8.9 player/world/biome access | Legacy render path | Medium | EntityBridge/WorldBridge should be designed later. |
| Keystrokes | Render-only | Legacy key/mouse state | Legacy render path | Medium | InputBridge should be evaluated later. |
| Ping Display | Render-only | Direct 1.8.9 `NetworkPlayerInfo` access | Legacy render path | Medium | Network/Game bridge may be needed later. |

## 4. Not Migrated / Legacy Systems

The following systems remain legacy/direct 1.8.9 and were not migrated in v0.5.24:

- Armor HUD
- Potion HUD
- ToggleSprint
- Crosshair
- Reach Display
- Combo Display
- Fullbright
- Time Changer
- RenderUtils
- CarbonMenuScreen
- HudLayoutEditorScreen
- ForgeEventBridge
- KeyInputHandler
- ConfigManager
- ProfileManager

These systems should be handled with explicit future plans, not incidental bridge usage.

## 5. Risk Classification Table

| System | Risk | Reason | Recommended approach |
| --- | --- | --- | --- |
| Armor HUD | High | ItemStack render, RenderItem, RenderHelper, GL lighting state, durability overlay, enchanted glint, null armor slots. | First safe step can be background/text bridge only; item render should stay legacy until a dedicated item render adapter exists. |
| Potion HUD | High | Potion icon texture, texture binding, GL state, potion API differences, effect ordering, duration/amplifier formatting. | First safe step can be background/text bridge only; icon rendering and potion data should stay legacy until texture/icon bridge work exists. |
| ToggleSprint | Medium-High | Movement/input state, player capabilities, sprint/sneak behavior. | Analyze after v0.6 planning; preserve non-cheat behavior. |
| Crosshair | Medium-High | Vanilla overlay render timing, GL state, custom crosshair draw. | Needs a separate safe overlay plan after render bridge boundaries are stable. |
| Reach Display | High | Entity/raycast/attack event logic and hit distance calculation. | Do not migrate before EntityBridge/EventBridge design. |
| Combo Display | High | Attack/damage event logic, entity state, hurtTime tracking. | Do not migrate before EntityBridge/EventBridge design. |
| Fullbright | Medium | Gamma setting and original brightness restore behavior. | May need Game/settings bridge later. |
| Time Changer | Medium | World time visual override and world access. | May need WorldBridge later. |
| RenderUtils | Medium | Shared by many HUD and UI paths. | Avoid one-shot migration; continue helper-based incremental work. |
| CarbonMenuScreen / HudLayoutEditorScreen | Medium-High | GUI screen inheritance, mouse input, rendering, scaling. | Do not move at v0.6 entry; stabilize runtime module bridge first. |
| ForgeEventBridge / KeyInputHandler | High | Forge event version differences and input timing. | Needs version-specific event adapters in a later phase. |
| ConfigManager / ProfileManager | Low-Medium | Mostly common, but module serialization and runtime values require care. | Can remain common; never persist runtime-only values. |

## 6. Completed v0.5.x Work

- Bridge API skeleton.
- `BridgeRegistry`.
- Passive 1.8.9 bridge implementations.
- `BridgeDiagnostics`.
- `RenderBridgeAccess` helper.
- FPS Display render-only bridge consumer.
- CPS Display render-only bridge consumer.
- Clock HUD render-only bridge consumer.
- Coordinates HUD render-only bridge consumer.
- Keystrokes render-only bridge consumer.
- Ping Display render-only bridge consumer.
- Module enabled state persistence hotfix.
- Armor/Potion bridge risk analysis.

## 7. Boundaries / What We Did Not Do

- No 1.7.10 port was started.
- No `build.gradle` multi-project or source-set split was added.
- No InputBridge usage was introduced.
- No EntityBridge usage was introduced.
- No WorldBridge usage was introduced.
- No GameBridge data abstraction was added for module data.
- No NetworkBridge exists.
- Armor/Potion bridge migration was not implemented.
- UI/Menu bridge migration was not implemented.
- Launcher/auth/cosmetic work was not started.

## 8. v0.6.0 Entry Criteria

Before v0.6 starts implementation work:

- Confirm the v0.5.x render-only consumer list remains stable.
- Complete manual restart QA for the module enabled-state persistence hotfix if not already done by the user.
- Choose an initial 1.8.9 / 1.7.10 strategy: branch, source-set, or multi-project.
- Identify common package candidates.
- Identify version-specific package candidates.
- Document 1.7.10 Forge, MCP, render, event, input, potion, item, and ping risks.
- Keep runtime-only values out of config/profile snapshots.
- Keep current runtime on Minecraft 1.8.9 until the strategy is explicit.

## 9. v0.6.x Proposed Roadmap

| Version | Proposed checkpoint |
| --- | --- |
| v0.6.0 | Multi-Version Runtime Preparation |
| v0.6.1 | 1.7.10 Environment Strategy / Branch or Source Layout Decision |
| v0.6.2 | Common vs Version-Specific Package Separation Plan |
| v0.6.3 | First 1.7.10 Experimental Runtime Setup in a separate branch or safe isolation |
| v0.6.4 | Low-risk module port candidate analysis: Clock/FPS/CPS |
| v0.6.5 | First low-risk 1.7.10 module prototype, only in isolated work |

This roadmap is a controlled planning path, not a promise to migrate all modules at once.

## 10. Recommended Next Step

Recommended:

- v0.6.0 Multi-Version Runtime Preparation.

Alternative if manual runtime validation is still incomplete:

- v0.5.25 Final v0.5.x Stability QA.

Known checkpoint note:

- The module enabled-state persistence hotfix has build/static validation, but if manual restart QA was not completed locally, it should be completed before v0.6 implementation work begins.
## v0.6.0 Follow-Up

- v0.6.0 Multi-Version Runtime Preparation has started in `MULTI_VERSION_RUNTIME_PREPARATION_v0.6.0.md`.
- The next phase documents performance-first architecture and PvP responsiveness / rod responsiveness principles.
- Current runtime remains 1.8.9, and no 1.7.10 code/dependency has been added yet.
