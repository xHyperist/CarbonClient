# Common vs Version-Specific Separation Plan v0.6.2

## 1. Purpose

This document classifies the current Carbon Client package structure before any physical package move or 1.7.10 branch work begins.

v0.6.2 is planning-only. It does not start the 1.7.10 port, does not change `build.gradle`, does not add dependencies, does not create source sets, does not create a branch, and does not move packages.

The goal is to identify what can stay common, what should become version-specific, and what needs adapters before an isolated 1.7.10 experiment.

## 2. Current Package Overview

Current source layout remains `src/main/java` with one 1.8.9 Forge runtime.

Observed package groups:

- `com.carbonclient.account`, `cloud`, `compatibility`, `cosmetic`, `launcher`, `permission`, `update`: mostly pure service placeholders and future platform systems.
- `com.carbonclient.bridge.api`, `bridge.registry`, `bridge.diagnostics`, `bridge.render`: common bridge-facing packages.
- `com.carbonclient.bridge.impl.v1_8_9`: version-specific passive bridge implementation.
- `com.carbonclient.client` and root `CarbonClient`: lifecycle/bootstrap and Forge integration.
- `com.carbonclient.common`: metadata.
- `com.carbonclient.config`, `profile`: mostly serialization/state persistence, with runtime snapshot dependency.
- `com.carbonclient.event`: mixed; base event bus is common, some event payloads import Minecraft classes.
- `com.carbonclient.event.bridge`: Forge event adapter, version-specific.
- `com.carbonclient.gui`: Minecraft GUI screen implementation, version-specific/mixed.
- `com.carbonclient.input`: Forge/LWJGL key handling, version-specific.
- `com.carbonclient.module`, `setting`: mostly common metadata/settings, but keybind pieces still touch LWJGL.
- `com.carbonclient.modules`: current 1.8.9 module implementations.
- `com.carbonclient.notification`: model is common; renderer is version-specific.
- `com.carbonclient.ui`: theme/components are closer to common; low-level render utilities are version-specific.
- `com.carbonclient.visual`: manager is mixed; Fullbright and Time Changer are version-specific.

## 3. Common Candidate Packages

Likely common now:

- `com.carbonclient.common.Reference`
- `com.carbonclient.setting.Setting`
- `BooleanSetting`
- `NumberSetting`
- `ModeSetting`
- `ColorSetting`
- `com.carbonclient.module.ModuleCategory`
- `com.carbonclient.module.ModuleManager`, with care around lifecycle/event registration.
- `com.carbonclient.event.Event`
- `EventBus`
- `EventListener`
- Notification data/model types such as `Notification` and `NotificationType`.
- Theme/design tokens such as `CarbonTheme`.
- Bridge API interfaces.
- `BridgeRegistry`.
- `BridgeDiagnostics` and `BridgeDiagnosticsReport`.
- `RenderBridgeAccess`.
- Roadmap and architecture documentation.
- Pure Java service placeholders if they avoid Minecraft/Forge/LWJGL imports.

Needs cleanup before fully common:

- `Module`: imports LWJGL `Keyboard` for key names/default behavior.
- `KeybindSetting`: imports LWJGL `Keyboard`.
- `ConfigManager` / `ProfileManager`: mostly common serialization, but tied to module snapshots, visual snapshots, notifications, and runtime wiring.
- UI components: layout may be common, but some flows are still coupled to Minecraft font/render usage through screens/utilities.

## 4. Version-Specific Candidate Packages

Likely version-specific:

- Root Forge mod entrypoint `CarbonClient`.
- `com.carbonclient.client.Client` because it wires Forge lifecycle, Forge event bus, and 1.8.9 bridge bootstrap.
- `com.carbonclient.event.bridge.ForgeEventBridge`.
- `com.carbonclient.input.KeyInputHandler`.
- `com.carbonclient.bridge.impl.v1_8_9`.
- `com.carbonclient.ui.render.RenderUtils`.
- `com.carbonclient.gui.CarbonMenuScreen`.
- `com.carbonclient.gui.HudLayoutEditorScreen`.
- `NotificationRenderer`.
- Modules that directly use `Minecraft.getMinecraft()`, `Gui`, `GlStateManager`, entity/world classes, item renderer, potion APIs, or LWJGL.
- `FullbrightVisual` because it touches gamma settings.
- `TimeChangerVisual` because it touches world time.
- Event payloads that wrap Minecraft `Entity`, `EntityPlayer`, or `DamageSource`.

Why version-specific:

- 1.7.10 and 1.8.9 differ in Forge event classes, MCP mappings, GUI APIs, render pipeline, item/potion APIs, keybinding internals, network/player info access, and entity/world behavior.

## 5. Mixed / Adapter-Needed Packages

Mixed systems:

- `Module`: metadata/lifecycle can be common, but key names and runtime registration need input/event adapters.
- `DraggableHudModule`: position/settings can be common, render/bounds integration needs render/game adapters.
- HUD modules: settings/layout can often be common, but data access and render implementations vary.
- `CarbonMenuScreen`: UI state and menu logic can be common later, but `GuiScreen`, input, scaling, and rendering are version-specific.
- `HudLayoutEditorScreen`: drag model can be common, but screen/input/render implementation is version-specific.
- `ConfigManager` / `ProfileManager`: serialization can remain common, but module snapshot compatibility must avoid runtime-only values.
- `KeybindSetting`: setting semantics can be common, key naming/state needs `InputBridge`.
- Visual modules: manager logic can be common, Fullbright/Time Changer data access is version-specific.
- `RenderUtils`: common drawing concepts exist, but low-level GL/Minecraft calls are version-specific.

Early physical movement is risky. These systems should be separated by adapter plans first.

## 6. Module Separation Table

| Module | Current Version Support | Common Candidate Parts | Version-Specific Parts | Needed Bridge/Adapter | Port Risk | Performance Notes | PvP/Rod Responsiveness Notes |
| --- | --- | --- | --- | --- | --- | --- | --- |
| FPS Display | 1.8.9 | Settings, layout, text format | FPS source, legacy render path | RenderBridge already used; GameBridge later if needed | Low | Keep text formatting light | Informational only |
| CPS Display | 1.8.9 | CPS display format, settings | Mouse event/click tracking | Event/Input adapter later | Low-Medium | Keep click list cleanup cheap | Must not add autoclicker or input delay |
| Clock HUD | 1.8.9 | Local/system time formatting, settings | Legacy render path | RenderBridge already used | Low | Java time formatting should stay cheap | No PvP mechanic impact |
| Coordinates HUD | 1.8.9 | Settings, line layout | Player/world/biome/direction reads | EntityBridge/WorldBridge later | Medium | Avoid per-frame heavy world queries | Informational only |
| Keystrokes | 1.8.9 | Settings/layout | Key/mouse/space pressed state | InputBridge later | Medium | Input display must stay lightweight | Must not increase input delay |
| Ping Display | 1.8.9 | Settings/layout/text format | `NetworkPlayerInfo` ping data | Network/Game bridge if needed | Medium | Avoid heavy server/player lookups | Helps user understand PvP feel |
| Armor HUD | 1.8.9 | Settings, panel/text concepts | ItemStack, RenderItem, RenderHelper, overlay/glint | ItemRenderBridge later | High | Item render/GL state is sensitive | Informational only |
| Potion HUD | 1.8.9 | Settings, text concepts | Potion data/icon texture/GL state | Texture/IconBridge and potion adapter later | High | Texture binding and ordering are sensitive | Informational only |
| ToggleSprint | 1.8.9 | Settings and HUD text concepts | KeyBinding, player movement/sprint state | InputBridge/EntityBridge/EventBridge later | Medium-High | Must not add per-tick overhead | Must not create cheat movement advantage |
| Crosshair | 1.8.9 | Settings and preview data | Vanilla overlay hook, GL rendering | Render/Event overlay adapter later | Medium-High | Overlay render must be minimal | No aim assist or combat advantage |
| Reach Display | 1.8.9 | Settings/text format | Attack event, entity/raycast/distance logic | EventBridge/EntityBridge later | High | Avoid heavy entity calculations | Informational only; no reach changes |
| Combo Display | 1.8.9 | Settings/text format | Attack/damage event, hurtTime tracking | EventBridge/EntityBridge later | High | Runtime state cleanup should stay cheap | Informational only; no combat automation |
| Fullbright | 1.8.9 | Visual setting metadata | Gamma access/restore | Game/settings adapter later | Medium | Avoid tick spam; preserve restore | No PvP mechanic change |
| Time Changer | 1.8.9 | Visual setting metadata | World time access/override | WorldBridge later | Medium | Avoid unnecessary world writes | Visual only; no server mechanic change |

## 7. Bridge / Adapter Needs

- `RenderBridge`: already used by FPS, CPS, Clock, Coordinates, Keystrokes, and Ping for text/background drawing.
- `InputBridge`: needed later for Keystrokes, ToggleSprint, keybind settings, and input handlers.
- `EventBridge`: needed later for Forge render/input/attack/damage/tick adapters.
- `EntityBridge`: needed later for Coordinates, Reach, Combo, ToggleSprint, and entity/player state.
- `WorldBridge`: needed later for Coordinates, Time Changer, biome, and world time access.
- Network/Game bridge: may be needed for Ping Display and server/player info differences.
- ItemRenderBridge: needed before Armor item rendering can move out of direct 1.8.9 code.
- Texture/IconBridge: needed before Potion icon rendering can move out of direct 1.8.9 code.

## 8. Performance-First Separation Rule

Common extraction must not reduce performance.

Rules:

- Do not use reflection in hot paths.
- Do not perform version lookup every frame.
- Keep adapter calls thin and cache-friendly.
- Reduce render tick object allocation.
- Use common abstractions only where they remove real duplication or isolate version-specific APIs.
- Disabled modules must not create tick/render cost.
- Config/profile IO must not run in render/tick paths.
- Do not add debug/log spam.
- Keep string formatting and text measurement lightweight.

## 9. PvP / Rod Responsiveness Rule

Input path abstraction must not increase rod/right click/item-use delay.

Rules:

- Keystrokes and CPS must remain lightweight.
- HUD/render systems must not create frame spikes during PvP.
- Config/profile saves must not continuously trigger during combat/render.
- Rod responsiveness is a performance/input-feel goal, not a cheat goal.

Forbidden:

- Rod cooldown bypass.
- Packet spam.
- Packet manipulation.
- Auto rod.
- Autoclicker.
- Reach, hitbox, velocity, or aim assist.
- Server-side mechanic manipulation.

## 10. Migration Order Recommendation

Recommended order:

1. No physical package move in v0.6.2.
2. v0.6.3 isolated branch preparation.
3. v0.6.4 low-risk module port candidate review.
4. First experiment should target minimal 1.7.10 bootstrap only, not full modules.

Avoid moving mixed packages until the isolated 1.7.10 branch proves the environment constraints.

## 11. v0.6.3 Entry Criteria

Before v0.6.3:

- 1.8.9 clean build remains successful.
- v0.6.2 separation plan is complete.
- No unplanned package moves are pending.
- Isolated branch naming and rollback plan are agreed.
- 1.7.10 ForgeGradle/JDK constraints are ready to test.
- Minimal bootstrap scope is defined.
- Performance-first and PvP responsiveness rules remain active.

## 12. What We Are Not Doing Yet

- Not starting the 1.7.10 port.
- Not adding 1.7.10 dependencies.
- Not changing `build.gradle`.
- Not creating source sets or multi-project structure.
- Not creating a branch automatically.
- Not physically moving packages.
- Not performing a large refactor.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not changing runtime behavior.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
