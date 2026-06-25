# Carbon Client Bridge Dependency Map v0.5.2

## 1. Purpose

This document maps the current Minecraft 1.8.9 Forge dependencies in Carbon Client before any real multi-version refactor begins.

The goal is to make future bridge work deliberate: know which classes can stay common, which classes are tied to 1.8.9 APIs, which bridge each dependency should move behind, and which systems need extra caution before a 1.7.10 port.

## 2. Scope

Covered systems:

- Core: `Client`, `Reference`, `Module`, `ModuleManager`, `ModuleCategory`, settings, config, profiles, notifications, visuals.
- Event: `EventBus`, `ForgeEventBridge`, Carbon event classes.
- GUI/UI: `CarbonMenuScreen`, `HudLayoutEditorScreen`, components, theme, `RenderUtils`.
- Modules: FPS, CPS, Keystrokes, ToggleSprint, Armor, Potion, Coordinates, Ping, Crosshair, Reach, Combo, Clock.
- Visuals: Fullbright and Time Changer.
- Bridge skeleton: Game, Render, Input, Event, Entity, World, Config, registry, 1.8.9 placeholder.

## 3. Current Runtime Rule

v0.5.2 is documentation-only for bridge migration.

- The existing runtime remains the current Minecraft 1.8.9 Forge implementation.
- Bridge APIs compile, but modules are not connected to them yet.
- No 1.7.10 code, dependency, source set, or Gradle multi-project structure is introduced.
- Direct `net.minecraft`, `net.minecraftforge`, and LWJGL usage remains where it already works.

## 4. Dependency Categories

### COMMON_READY

Code that has no meaningful Minecraft/Forge dependency and can probably stay common.

### COMMON_WITH_MINOR_ADAPTER

Code that is mostly common, but has small access points to input, render, game directory, or version-specific values.

### VERSION_SPECIFIC_1_8_9

Code directly coupled to Minecraft 1.8.9, Forge events, GUI classes, render classes, item/potion/entity APIs, or LWJGL state.

### RISKY_PORT

Code that depends on fragile event timing, overlay hooks, combat feedback, or version-specific gameplay/render behavior. These should be delayed until safer bridge boundaries exist.

## 5. Common-Ready Classes

These classes can likely remain common with little or no change:

- `com.carbonclient.common.Reference`: static metadata only.
- `com.carbonclient.module.ModuleCategory`: enum metadata.
- `com.carbonclient.module.ModuleManager`: registration, lookup, toggle, default reset, notification handoff.
- `com.carbonclient.event.Event`
- `com.carbonclient.event.EventBus`
- `com.carbonclient.event.EventListener`
- `com.carbonclient.setting.Setting`
- `com.carbonclient.setting.impl.BooleanSetting`
- `com.carbonclient.setting.impl.NumberSetting`
- `com.carbonclient.setting.impl.ModeSetting`
- `com.carbonclient.setting.impl.ColorSetting`
- `com.carbonclient.notification.Notification`
- `com.carbonclient.notification.NotificationType`
- `com.carbonclient.notification.NotificationManager`
- `com.carbonclient.profile.Profile`
- `com.carbonclient.profile.ProfileManager`
- `com.carbonclient.visual.VisualManager`
- Future service placeholders: `AccountService`, `CloudConfigService`, `CosmeticService`, `LauncherBridge`, `PermissionService`, `CompatibilityService`, `UpdateService`, `UpdateInfo`.
- Bridge API skeletons: `GameBridge`, `RenderBridge`, `InputBridge`, `EventBridge`, `EntityBridge`, `WorldBridge`, `ConfigBridge`, `BridgeRegistry`, `BridgeVersionInfo`.

Notes:

- `ProfileManager` is common-ready because it works through `ConfigManager` snapshots and `ProfileStorage`.
- `VisualManager` is common-ready as a coordinator, but individual visual implementations are not.

## 6. Common With Minor Adapter Classes

These classes are mostly reusable, but need one or more small bridges before they become version-neutral:

- `com.carbonclient.module.Module`: mostly common, but imports LWJGL `Keyboard` for key name/default handling. Future target: `InputBridge`.
- `com.carbonclient.module.DraggableHudModule`: common HUD position contract, but render/bounds implementations remain version-specific.
- `com.carbonclient.setting.impl.KeybindSetting`: imports LWJGL `Keyboard`. Future target: `InputBridge.getKeyName`.
- `com.carbonclient.config.ConfigManager`: common JSON snapshot logic, but receives game directory from 1.8.9 client bootstrap. Future target: `ConfigBridge` for version-specific paths/migrations.
- `com.carbonclient.profile.ProfileStorage`: common file persistence, but path selection depends on game directory. Future target: `ConfigBridge`.
- `com.carbonclient.ui.theme.CarbonTheme`: common design tokens.
- UI components `ButtonComponent`, `ToggleComponent`, `SliderComponent`, `CardComponent`, `ColorPickerComponent`: layout logic can stay common, but current constructors/methods use Minecraft `FontRenderer`. Future target: `RenderBridge`.
- `com.carbonclient.gui.CarbonMenuScreen`: state and menu flow can remain conceptually common, but the class extends `GuiScreen` and uses LWJGL input. Future target: `GameBridge`, `RenderBridge`, `InputBridge`.
- `com.carbonclient.gui.HudLayoutEditorScreen`: drag/drop and bounds logic can remain common, but GUI base class and input are 1.8.9-specific. Future target: `GameBridge`, `RenderBridge`, `InputBridge`.

## 7. Version-Specific 1.8.9 Classes

These classes directly depend on Minecraft 1.8.9 / Forge / LWJGL APIs:

- `com.carbonclient.CarbonClient`: Forge `@Mod` entrypoint and FML lifecycle events.
- `com.carbonclient.client.Client`: Forge lifecycle event types and Forge event bus registration.
- `com.carbonclient.input.KeyInputHandler`: `Minecraft`, Forge `KeyInputEvent`, LWJGL `Keyboard`, GUI screen opening.
- `com.carbonclient.event.bridge.ForgeEventBridge`: Forge overlay, mouse, tick, attack, and hurt events.
- `com.carbonclient.event.impl.AttackEntityEvent`: wraps `EntityPlayer` and `Entity`.
- `com.carbonclient.event.impl.PlayerDamageEvent`: wraps `EntityPlayer`, `Entity`, and `DamageSource`.
- `com.carbonclient.notification.NotificationRenderer`: `Minecraft`, `FontRenderer`, `ScaledResolution`.
- `com.carbonclient.ui.render.RenderUtils`: `Minecraft`, `FontRenderer`, `Gui`, `ScaledResolution`, GL11 scissor/clipping.
- `FPSDisplayModule`: `Minecraft`, `Gui`, `GlStateManager`, LWJGL key constants.
- `CPSDisplayModule`: `Minecraft`, `Gui`, `GlStateManager`, mouse event dependency through Carbon event.
- `KeystrokesModule`: `Minecraft`, `Gui`, `GlStateManager`, `KeyBinding`, LWJGL `Keyboard`/`Mouse`.
- `ArmorHudModule`: `Minecraft`, `GlStateManager`, `RenderHelper`, `RenderItem`, `ItemStack`.
- `PotionHudModule`: `Minecraft`, `GlStateManager`, `I18n`, `Potion`, `PotionEffect`, `ResourceLocation`.
- `CoordinatesHudModule`: `Minecraft`, `Gui`, `GlStateManager`, `BlockPos`, biome/player access.
- `PingDisplayModule`: `Minecraft`, `NetworkPlayerInfo`, `Gui`, `GlStateManager`.
- `ClockHudModule`: `Minecraft`, `Gui`, `GlStateManager`; time logic itself is common.
- `FullbrightVisual`: `Minecraft.gameSettings.gammaSetting`.
- `TimeChangerVisual`: `Minecraft`, `WorldClient`, client world time mutation.

## 8. Risky Port Classes

These classes need extra validation before being moved or ported:

- `CrosshairModule`: center-fixed custom crosshair and vanilla crosshair hide/render event behavior depend on overlay timing.
- `ToggleSprintModule`: manipulates `KeyBinding` state and sprint/sneak behavior; 1.7.10 key internals may differ.
- `ReachDisplayModule`: informational only, but relies on attack event timing, entity bounding boxes, eye height, and hit target references.
- `ComboDisplayModule`: informational only, but relies on pending hit confirmation, `EntityLivingBase.hurtTime`, and local player damage events.
- Future Scoreboard customization: vanilla sidebar render hook was explicitly deferred for research.
- Future Zoom: should wait for OptiFine/launcher compatibility planning.
- Future Block Overlay, Chat Mod, Weather Changer, Capture/Recording: event/render hooks need separate research.

## 9. Bridge Mapping

### GameBridge

Needed by:

- `Client` for runtime version/game state later.
- `KeyInputHandler` for current screen and display screen actions.
- `CarbonMenuScreen` and `HudLayoutEditorScreen` for screen/game state.
- HUD modules that call `Minecraft.getMinecraft()` directly.
- `NotificationRenderer` and `RenderUtils` for display size and Minecraft instance access.

Responsibilities:

- `Minecraft` instance access
- in-game/world/player null state
- singleplayer/multiplayer state
- display width/height

### RenderBridge

Needed by:

- `RenderUtils`
- `NotificationRenderer`
- UI components using `FontRenderer`
- all text/rect HUD modules
- `ArmorHudModule` for future item rendering extension
- `PotionHudModule` for future icon rendering extension
- `CrosshairModule`

Responsibilities:

- draw rect/text
- string width/font height
- scaled resolution
- scissor/clipping
- GL state safety
- later: item/icon rendering

### InputBridge

Needed by:

- `Module`
- `KeybindSetting`
- `KeyInputHandler`
- `CarbonMenuScreen`
- `HudLayoutEditorScreen`
- `KeystrokesModule`
- `ToggleSprintModule`

Responsibilities:

- key state
- mouse state
- key names
- keybind handling
- screen-focused text/key input separation

### EventBridge

Needed by:

- `ForgeEventBridge`
- `Render2DEvent`
- `MouseButtonEvent`
- `AttackEntityEvent`
- `PlayerDamageEvent`
- `ClientTickEvent`
- `CrosshairRenderEvent`

Responsibilities:

- render2D dispatch
- key input dispatch
- mouse input dispatch
- attack entity dispatch
- local player damage dispatch
- client tick dispatch
- world load/unload dispatch
- vanilla overlay hooks such as crosshair

### EntityBridge

Needed by:

- `ReachDisplayModule`
- `ComboDisplayModule`
- `CoordinatesHudModule`
- `ToggleSprintModule`
- future PvP/info HUD modules

Responsibilities:

- entity/player/living checks
- position
- eye height
- bounding box
- entity id
- hurt time/damage animation state

### WorldBridge

Needed by:

- `CoordinatesHudModule`
- `TimeChangerVisual`
- `PingDisplayModule` indirectly through server state
- future weather/time/biome modules

Responsibilities:

- world presence
- world time
- biome at player
- dimension
- server/world state

### ConfigBridge

Needed by:

- `ConfigManager`
- `ProfileStorage`
- future version-specific config migration

Responsibilities:

- game directory
- config path decisions
- version-specific migration flags
- unsupported module/setting migration behavior

## 10. Module-by-Module Dependency Notes

| Module/System | Category | Current Direct Dependencies | Future Bridge Needs | Port Risk |
| --- | --- | --- | --- | --- |
| FPS Display | VERSION_SPECIFIC_1_8_9 | `Minecraft`, `Gui`, `GlStateManager` | GameBridge, RenderBridge | Low |
| CPS Display | VERSION_SPECIFIC_1_8_9 | `Minecraft`, `Gui`, `GlStateManager`, mouse event path | RenderBridge, EventBridge, InputBridge | Low |
| Keystrokes | VERSION_SPECIFIC_1_8_9 | `Minecraft`, `KeyBinding`, LWJGL `Keyboard`/`Mouse` | InputBridge, RenderBridge | Low-Medium |
| Clock HUD | VERSION_SPECIFIC_1_8_9 render only | Java time plus MC render classes | RenderBridge | Low |
| Coordinates HUD | VERSION_SPECIFIC_1_8_9 | player/world/biome, `BlockPos` | GameBridge, EntityBridge, WorldBridge, RenderBridge | Low |
| Armor HUD | VERSION_SPECIFIC_1_8_9 / RISKY_PORT | `ItemStack`, `RenderItem`, `RenderHelper`, item overlay and GL lighting state | RenderBridge for text/background first; future RenderItemBridge for items | Medium-High |
| Potion HUD | VERSION_SPECIFIC_1_8_9 / RISKY_PORT | `Potion`, `PotionEffect`, icon texture binding, `I18n`, GL state | RenderBridge for text/background first; future icon/texture bridge for icons | Medium-High |
| Ping Display | VERSION_SPECIFIC_1_8_9 | `NetworkPlayerInfo` | GameBridge/WorldBridge or future NetworkBridge | Medium |
| ToggleSprint | RISKY_PORT | `KeyBinding`, player movement/sneak/sprint state | InputBridge, EntityBridge, EventBridge | High |
| Crosshair | RISKY_PORT | overlay/crosshair hook, mouse movement | EventBridge, RenderBridge, InputBridge | High |
| Reach Display | RISKY_PORT | attack event, entity bounding box, eye height | EventBridge, EntityBridge | High |
| Combo Display | RISKY_PORT | attack event, `hurtTime`, damage event | EventBridge, EntityBridge | High |
| Fullbright | VERSION_SPECIFIC_1_8_9 | `gameSettings.gammaSetting` | GameBridge or VisualBridge later | Medium |
| Time Changer | VERSION_SPECIFIC_1_8_9 / RISKY_PORT | `WorldClient.setWorldTime` | WorldBridge, EventBridge | Medium-High |
| Notification Renderer | VERSION_SPECIFIC_1_8_9 | font, scaled resolution | RenderBridge, GameBridge | Low |
| Carbon Menu | COMMON_WITH_MINOR_ADAPTER | `GuiScreen`, LWJGL input, render utilities | GameBridge, RenderBridge, InputBridge | Medium |
| HUD Editor | COMMON_WITH_MINOR_ADAPTER | `GuiScreen`, mouse/key input, render utilities | GameBridge, RenderBridge, InputBridge | Medium |

## 11. Migration Order

### Phase A - Documentation Only

- Current v0.5.2.
- No runtime migration.
- Keep 1.8.9 build stable.

### Phase B - Low-Risk Bridge Implementation

- Started in v0.5.3 with passive 1.8.9 implementations:
  - `V189GameBridge`
  - `V189InputBridge`
  - `V189RenderBridge`
  - `V189BridgeBootstrap`
- These bridges are registered through `BridgeRegistry`.
- Existing modules are not migrated yet and still use the direct 1.8.9 path.
- v0.5.4 validated passive bridge runtime safety:
  - Bridge bootstrap remains idempotent.
  - `BridgeRegistry` ignores null registrations.
  - Game/Input/Render bridge null-safety was checked.
  - API packages remain free of Minecraft/Forge imports.
- v0.5.5 added passive diagnostics validation:
  - `BridgeDiagnostics` can inspect core bridge readiness.
  - `BridgeDiagnosticsReport` captures bridge availability without user-facing UI.
  - Core passive bridges can be checked before consumer migration.
  - Event, Entity, World, and Config bridges may remain unavailable at this phase.

### Phase C - Low-Risk HUD Modules

- Started in v0.5.6 with FPS Display bridge-assisted render prototype.
- FPS Display remains fallback-safe and keeps its legacy render path.
- v0.5.7 completed FPS Display bridge consumer QA and fallback validation.
- v0.5.8 continued Phase C with CPS Display bridge-assisted render prototype.
- v0.5.9 completed CPS Display bridge consumer QA.
- v0.5.10 continues Phase C with Clock HUD bridge-assisted render prototype.
- v0.5.11 completed Clock HUD bridge consumer QA.
- v0.5.12 completed low-risk bridge consumer QA for FPS, CPS, and Clock HUD together.
- v0.5.13 continues Phase C with Coordinates HUD render-only bridge prototype.
- v0.5.14 completed Coordinates HUD bridge consumer QA.
- v0.5.15 completed low-risk render bridge consumer QA pass II for FPS, CPS, Clock, and Coordinates.
- v0.5.16 continues Phase C with Keystrokes partial render bridge prototype.
- v0.5.17 completed Keystrokes bridge consumer QA.
- v0.5.18 completed render bridge consumer QA pass III for FPS, CPS, Clock, Coordinates, and Keystrokes.
- v0.5.19 completed RenderBridge helper extraction for safe readiness and metric access.
- v0.5.20 completed RenderBridge helper QA.
- v0.5.21 continues Phase C with Ping Display render-only bridge prototype.
- v0.5.22 completed Ping Display bridge consumer QA.
- v0.5.23 fixed module enabled-state persistence and completed Armor/Potion bridge risk analysis.
- v0.5.24 completed the Multi-Version Bridge Phase Review and confirmed the current render-only consumers, legacy system boundaries, and v0.6.0 transition plan.
- FPS Display, CPS Display, Clock HUD, Coordinates HUD, Keystrokes, and Ping Display remain fallback-safe bridge-assisted render consumers.
- Render-only bridge consumers are stable with legacy fallback.
- The helper centralizes safe RenderBridge readiness and metric access; consumers still own module-specific render and fallback decisions.
- Coordinates HUD remains render-only bridge-assisted; entity/world data abstraction is future work.
- Keystrokes remains render-only bridge-assisted; input abstraction remains future work and `InputBridge` is still not used.
- Ping Display remains render-only bridge-assisted; ping data still comes from direct 1.8.9 network/player info access.
- Ping data abstraction remains future work.
- Armor HUD risk classification:
  - Safe first step: background/text/durability text may use `RenderBridge` later.
  - Keep direct 1.8.9 for armor item list, `ItemStack`, `RenderItem`, `RenderHelper`, item overlay, durability bar, enchanted glint, z-level/lighting state, and null slot handling.
  - Full bridge migration should wait for a dedicated RenderItemBridge or item render adapter.
- Potion HUD risk classification:
  - Safe first step: background/text, potion name, duration text, and amplifier text may use `RenderBridge` later.
  - Keep direct 1.8.9 for active potion effect data, potion icon texture binding, icon draw, texture atlas coordinates, GL blend/alpha state, effect ordering, and vanilla potion internals.
  - Full bridge migration should wait for a dedicated icon/texture bridge or potion render adapter.
- Data access remains direct 1.8.9 until bridge abstractions are explicitly designed.
- Continue moving one low-risk module at a time:
  - v0.6.0 Multi-Version Runtime Preparation
  - HUD render utility abstraction later
- Build after each module.

### v0.5.24 Review Result

- Current render-only bridge consumers are FPS Display, CPS Display, Clock HUD, Coordinates HUD, Keystrokes, and Ping Display.
- Armor HUD remains high risk because item rendering depends on `ItemStack`, `RenderItem`, `RenderHelper`, GL lighting state, overlays, durability, and enchanted glint behavior.
- Potion HUD remains high risk because icon rendering depends on texture binding, GL state, potion API behavior, effect ordering, duration text, and amplifier text.
- ToggleSprint, Crosshair, Reach Display, Combo Display, Fullbright, Time Changer, RenderUtils, UI screens, event adapters, and input handling remain legacy/direct 1.8.9.
- v0.6.0 should start with runtime preparation and version strategy, not an immediate 1.7.10 port.

### v0.6.0 Transition Note

- v0.6.0 starts multi-version runtime preparation only.
- Current render-only bridge consumers remain FPS Display, CPS Display, Clock HUD, Coordinates HUD, Keystrokes, and Ping Display.
- No new bridge consumer was added.
- No 1.7.10 runtime, dependency, source-set, or `build.gradle` change was introduced.
- Future adapter needs:
  - InputBridge for Keystrokes and keybind/input state.
  - EntityBridge/WorldBridge for Coordinates, Reach Display, and Combo Display.
  - Network/Game bridge for Ping Display if needed.
  - Item render bridge for Armor HUD.
  - Texture/icon bridge for Potion HUD.
- Performance-first and PvP responsiveness rules should guide future bridge work so abstraction does not create frame spikes or input delay.
### v0.6.1 Transition Note

- v0.6.1 is strategy-only and adds no new bridge consumer.
- Current render-only bridge consumers remain FPS Display, CPS Display, Clock HUD, Coordinates HUD, Keystrokes, and Ping Display.
- Initial 1.7.10 work should happen on an isolated experimental branch.
- The stable 1.8.9 branch should not receive risky `build.gradle`, dependency, source-set, or multi-project changes yet.
- Future adapter needs remain unchanged: InputBridge for input-heavy systems, Entity/World bridges for entity/world systems, Network/Game bridge for ping if needed, item render bridge for Armor, and texture/icon bridge for Potion.
### Phase D - UI Bridge Preparation

- Introduce `RenderBridge` into `RenderUtils`.
- Gradually move UI components from direct `FontRenderer` use to render abstraction.
- Keep `CarbonMenuScreen` and `HudLayoutEditorScreen` behavior unchanged.

### Phase E - Medium-Risk Modules

- Armor HUD
- Potion HUD
- Ping Display
- ToggleSprint
- Fullbright
- Time Changer

### Phase F - Risky Modules

- Crosshair
- Reach Display
- Combo Display
- Future Scoreboard, Zoom, Block Overlay, Chat, Weather, Capture work only after dedicated hook research.

### Phase G - 1.7.10 Environment

- Add separate 1.7.10 workspace/project only after the 1.8.9 bridge proof of concept remains stable.

### Phase H - Launcher / Version Selector Later

- Add launcher/version selector after both versions boot reliably.

## 12. Risks Before 1.7.10

- Forge event names and timing differ between 1.8.9 and 1.7.10.
- Overlay/crosshair/scoreboard hooks may not map cleanly.
- `ItemStack`, potion, ping, and team/formatting APIs may differ.
- Sprint/sneak key state manipulation may need a separate 1.7.10 implementation.
- PvP info HUD modules must remain informational and must never alter combat behavior.
- Config/profile snapshots must tolerate missing or unsupported settings.

## 13. Rules for Future Bridge Refactors

- Move one system at a time.
- Keep the 1.8.9 build passing after every bridge step.
- Do not change gameplay behavior while extracting bridge boundaries.
- Do not introduce 1.7.10 dependencies into the 1.8.9 build.
- Keep unknown config/profile fields safe and ignored.
- Do not reintroduce Scoreboard, Zoom, Block Overlay, Chat, Weather, or Capture as active features during bridge work.
- Keep Reach and Combo informational only: no packet, hitbox, reach, attack cancel, damage cancel, aura, triggerbot, autoclicker, or aim assist behavior.

## 14. What Was Not Changed in v0.5.2

- No code was moved to the bridge.
- No bridge implementation was written.
- No module was connected to bridge APIs.
- No 1.7.10 port was started.
- No `build.gradle` or source-set change was made.
- No launcher, auth, cosmetic, cloud, website, or admin panel code was added.
- No new module, HUD, visual, or gameplay feature was added.
