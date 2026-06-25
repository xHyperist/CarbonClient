# Low-Risk 1.7.10 Port Candidate Review v0.6.4

## 1. Purpose

This document reviews which Carbon Client modules are safest to consider for the first isolated Minecraft 1.7.10 experimental branch.

v0.6.4 is planning-only. It does not start the 1.7.10 port, does not create a branch, does not add dependencies, does not change `build.gradle`, does not move packages, and does not change runtime behavior.

The goal is to choose early port candidates that keep the stable 1.8.9 runtime protected while preserving Carbon Client's performance-first and legit PvP responsiveness goals.

## 2. Current Stable Runtime

Current stable state:

- Runtime: Minecraft Forge 1.8.9.
- Version: 0.6.4.
- Build system: existing 1.8.9 ForgeGradle setup.
- `build.gradle`: still stable 1.8.9 only.
- 1.7.10 implementation: not started.
- Source-set split: not present.
- Multi-project layout: not present.

Current render-only bridge-assisted consumers:

- FPS Display.
- CPS Display.
- Clock HUD.
- Coordinates HUD.
- Keystrokes.
- Ping Display.

## 3. Candidate Review Method

Each module is reviewed by these criteria:

- Render dependency: text-only, rectangle/background, item render, icon texture, overlay, GL state.
- Data/input dependency: Java-only, Minecraft FPS, mouse/key state, player/world, network/player info, entity events.
- Minecraft/Forge imports: direct 1.8.9 APIs increase port risk.
- 1.7.10 API difference risk: mappings, lifecycle, GUI/render, entity/world, network, potion/item APIs.
- Bridge/adapter need: RenderBridge, InputBridge, EntityBridge, WorldBridge, Network/Game bridge, ItemRenderBridge, Texture/IconBridge.
- Performance risk: render tick overhead, allocation, logging, IO, disabled module cost.
- PvP/rod responsiveness risk: input, movement, combat, packet, right click, item-use path sensitivity.
- First experiment suitability: whether the module is appropriate after minimal bootstrap.

## 4. Low-Risk Candidates

### Clock HUD

- Risk: Low.
- Uses Java/system local time.
- No player/world/entity dependency.
- Render-only bridge-assisted path already exists.
- Good first real module candidate after minimal 1.7.10 bootstrap.

### FPS Display

- Risk: Low.
- Simple text/background HUD.
- Render-only bridge-assisted path already exists.
- FPS value source must be checked in 1.7.10, but the module has low behavioral complexity.
- Good first or second real module candidate.

### CPS Display

- Risk: Low-Medium.
- Simple render path, but mouse click tracking is input-sensitive.
- Render-only bridge-assisted path already exists.
- Should come after Clock/FPS once basic input behavior is confirmed.

Recommended safest early order:

1. Clock HUD.
2. FPS Display.
3. CPS Display.

## 5. Medium-Risk Candidates

### Keystrokes

- Risk: Medium.
- Render-only bridge-assisted path exists, but pressed state is input-sensitive.
- W/A/S/D, LMB/RMB, and SPACE behavior must be validated against 1.7.10 input APIs.
- Future InputBridge may be needed.
- Better as a second-wave candidate, not the first module.

### Ping Display

- Risk: Medium.
- Render path is simple, but ping data depends on network/player info APIs.
- 1.7.10 network/player info differences must be checked before porting.
- Future Network/Game bridge may be needed.

### Coordinates HUD

- Risk: Medium.
- Render-only bridge-assisted path exists, but data comes from player/world/biome APIs.
- Entity/player/world/biome differences make it unsuitable as the first module.
- Future EntityBridge/WorldBridge may be needed.

### Fullbright

- Risk: Medium.
- Depends on game options/gamma state and original brightness restore behavior.
- Could be portable later with a Game/settings bridge, but should not be first.

### Time Changer

- Risk: Medium.
- Depends on world time access and visual override behavior.
- Future WorldBridge may be needed.
- Should not be in the first wave.

## 6. High-Risk / Deferred Modules

### Armor HUD

- Risk: High.
- ItemStack render, RenderItem, RenderHelper, GL state, overlays, durability, enchant glint, and null armor slots are version-sensitive.
- First safe step later might be background/text only, while item render remains legacy.
- Do not include in the first 1.7.10 experiment.

### Potion HUD

- Risk: High.
- Potion effect API, icon texture, texture bind/draw, duration/amplifier formatting, and GL state are version-sensitive.
- First safe step later might be background/text only, while icon render remains legacy.
- Do not include in the first 1.7.10 experiment.

### ToggleSprint

- Risk: Medium-High.
- Input/movement state and legit PvP boundaries are sensitive.
- Must not alter server-side movement mechanics or input feel.
- Defer until input/movement strategy is mature.

### Crosshair

- Risk: Medium-High.
- Overlay render and GL state are sensitive.
- Defer until render/overlay behavior is well understood in 1.7.10.

### Reach Display

- Risk: High.
- Entity/raycast/attack event logic is version-sensitive and sits near cheat-boundary concerns.
- Requires careful Entity/Event bridge design before porting.

### Combo Display

- Risk: High.
- Attack/damage event logic, entity state, and hurtTime tracking are version-sensitive.
- Requires careful Entity/Event bridge design before porting.

### Deferred Medium-Risk Visuals

- Fullbright is deferred from the first wave because gamma/options access is version-specific and original brightness restore behavior must stay safe.
- Time Changer is deferred from the first wave because world time access is version-specific and must remain visual-only.

## 7. Recommended First Experiment Order

Phase 1 - Minimal Bootstrap:

- Forge 1.7.10 environment.
- CarbonClient lifecycle.
- Basic init logs.
- Bridge skeleton feasibility check.
- No HUD modules yet.

Phase 2 - First Low-Risk HUD Candidate:

- Clock HUD or FPS Display.

Phase 3 - Next Low-Risk Candidates:

- FPS Display.
- CPS Display.

Phase 4 - Medium-Risk Candidates:

- Keystrokes.
- Ping Display.
- Coordinates HUD.

Deferred:

- Armor HUD.
- Potion HUD.
- ToggleSprint.
- Crosshair.
- Reach Display.
- Combo Display.
- Fullbright.
- Time Changer.

Recommended first real module order:

1. Clock HUD.
2. FPS Display.
3. CPS Display.

Reason:

- These modules are mostly text/background HUDs.
- They have fewer world/entity/input dependencies.
- They are good probes for RenderBridge portability.
- They have low performance risk when disabled and low render complexity when enabled.

## 8. Module Candidate Table

| Module | Risk Level | Why | First Experiment Suitability | Needed Bridge/Adapter | Performance Notes | PvP/Rod Notes |
| --- | --- | --- | --- | --- | --- | --- |
| Clock HUD | Low | Java/system time and simple text render | Best first real module | RenderBridge | Keep formatting lightweight | No input/combat path impact |
| FPS Display | Low | Simple text render; FPS source must be checked | Strong early candidate | RenderBridge | Avoid per-frame allocation beyond existing behavior | Useful PvP visibility, no input impact |
| CPS Display | Low-Medium | Mouse click tracking is input-sensitive | After Clock/FPS | RenderBridge now, Input review later | Keep click list trimming lightweight | Must not add autoclicker or alter click timing |
| Keystrokes | Medium | Key/mouse state differs across versions | Second wave | RenderBridge now, InputBridge later | Very lightweight hot path required | Must not increase input delay |
| Ping Display | Medium | Network/player info is version-specific | After network API review | RenderBridge now, Network/Game bridge later | Simple render, avoid network polling overhead | Useful PvP visibility, no packet manipulation |
| Coordinates HUD | Medium | Player/world/biome dependencies | After world/entity API review | RenderBridge now, Entity/World bridge later | Avoid expensive biome/world lookups | No combat/input path impact |
| Armor HUD | High | Item render, overlays, GL state, durability/glint | Deferred | ItemRenderBridge later | Item render state can be expensive | No PvP mechanic changes |
| Potion HUD | High | Potion API, icon texture, GL state | Deferred | Texture/IconBridge later | Icon/texture state must be controlled | No potion mechanic changes |
| ToggleSprint | Medium-High | Input/movement logic | Deferred | Input/Game adapter later | Must not run heavy tick logic | Must stay legit; no sprint advantage |
| Crosshair | Medium-High | Overlay and GL state | Deferred | Overlay/render adapter later | Avoid overlay frame spikes | No aim/reach assistance |
| Reach Display | High | Entity/raycast/attack event logic | Deferred | Entity/Event bridge later | Combat event code must be lean | Cheat boundary sensitive |
| Combo Display | High | Damage/attack/entity state logic | Deferred | Entity/Event bridge later | Event tracking must be lean | Cheat boundary sensitive |
| Fullbright | Medium | Gamma/options access | Later | Game/settings bridge later | Avoid repeated options writes | No combat mechanic changes |
| Time Changer | Medium | World time visual access | Later | WorldBridge later | Avoid world tick overhead | Visual only, no server-side manipulation |

## 9. Performance-First Candidate Rule

First 1.7.10 port candidates must protect FPS and frame-time stability. Carbon Client must keep its high-FPS / performance-first client goal during any future port experiment.

Rules:

- Early modules should have low render cost.
- Render tick allocation should remain low.
- No disk IO in render/tick paths.
- No per-frame log spam.
- Bridge/version abstraction must stay thin.
- Disabled modules must not create meaningful tick/render overhead.
- First 1.7.10 branch tests should record FPS and frame-time observations.

## 10. PvP / Rod Responsiveness Candidate Rule

The first 1.7.10 experiments must avoid input, movement, and combat logic unless specifically being reviewed. Rod responsiveness is a legit client feel/performance target, not a cheat target.

Rules:

- Rod/right click/item-use paths must not be delayed.
- Keystrokes/CPS input behavior must be watched carefully when they are eventually ported.
- HUD render must not create frame spikes during PvP.
- Config/profile save must not run continuously during combat/render.
- ToggleSprint, Reach, and Combo are deferred because they are sensitive to input, movement, entity, and combat behavior.

Forbidden:

- Rod cooldown bypass.
- Packet spam.
- Packet manipulation.
- Auto rod.
- Autoclicker.
- Reach/hitbox/velocity/aim assist.
- Server-side mechanic manipulation.

## 11. v0.6.5 Recommended Next Step

Recommended next step:

- v0.6.5 - First isolated 1.7.10 runtime experiment readiness checklist, or first experimental branch preparation if the user decides to create the branch manually.

If the experimental branch is not ready yet, v0.6.5 can remain a final pre-branch stability checkpoint for 1.8.9.

## 12. What We Are Not Doing Yet

- No branch is created.
- No 1.7.10 port is started.
- No 1.7.10 dependency is added.
- `build.gradle` is not changed.
- No source-set or multi-project layout is created.
- No package is physically moved.
- No new mod is added.
- No new bridge consumer is added.
- No runtime behavior is changed.
- No launcher/auth/cosmetic system is started.
- No cheat/PvP advantage feature is added.
