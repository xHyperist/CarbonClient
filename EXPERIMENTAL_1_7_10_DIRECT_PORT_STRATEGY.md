# Experimental 1.7.10 Direct Port Strategy

## Purpose

This checkpoint defines the next direction for the `experimental/1.7.10-runtime` branch.

Going forward, 1.7.10 modules should not be redesigned from scratch. The 1.8.9 Carbon Client implementation is the source of truth for design, spacing, colors, layout, settings, behavior, and user experience. The 1.7.10 branch should first inspect the matching 1.8.9 class, then port it with the smallest safe 1.7.10 adaptation.

## Core Rule

1.7.10 must target user-facing parity with 1.8.9.

- Do not create a different 1.7.10-only HUD design.
- Do not change formats, defaults, spacing, colors, or behavior unless a 1.7.10 API difference forces a small adapter.
- Do not add new features during port work.
- Do not port gameplay-changing or PvP advantage behavior.
- Prefer direct copy of common logic, then narrow version-specific fixes.
- Keep runtime hot paths lightweight: no render tick disk IO, no per-frame logging, no heavy reflection.

## Current Experimental State

The experimental branch currently has minimal bootstrap HUD prototypes:

- Clock HUD
- FPS Display
- CPS Display
- Shared `ExperimentalHudRenderer`

These prototypes validate rendering and basic parity direction only. They are not the final module system port.

## Direct-Port Decision Table

| System | Direct Copy? | 1.7.10 Adaptation | Adapter Needed? | Decision |
| --- | --- | --- | --- | --- |
| FPS Display | Mostly yes | `Minecraft.getDebugFPS()` is not available in 1.7.10; choose a parity-safe FPS source | Small runtime adapter likely | First direct-port candidate after module foundation |
| Clock HUD | Mostly yes | Java time formatting in 1.8.9 uses `java.time`, which is not safe for Java 6 source compatibility; use `SimpleDateFormat` or a small time adapter | Small time formatting adapter | Safest direct-port candidate |
| CPS Display | Mostly yes | Mouse event bridge differs; LMB-only and 1000 ms deque logic should be copied | Mouse input/event adapter | Early direct-port candidate after FPS/Clock |
| Keystrokes | Partial | KeyBinding, `Keyboard`, `Mouse`, layout, pressed color, and multi-box rendering must be checked against 1.7.10 names | Input adapter later | Medium risk; port after CPS |
| Ping Display | Partial | 1.8.9 `NetworkPlayerInfo` does not map directly to 1.7.10; ping source must be researched | Network/game adapter | Medium risk; data source first |
| Coordinates HUD | Partial | 1.8.9 `BlockPos` is not available in 1.7.10; player/world/biome access must be adapted | World/entity adapter | Medium risk; render/settings can copy first |
| Armor HUD | No full direct copy | ItemStack access may be similar, but render item, overlays, GL state, durability, and held item names differ | Item render adapter | High risk; defer |
| Potion HUD | No full direct copy | Potion API, icon texture binding, duration text, and I18n behavior differ | Texture/icon/potion adapter | High risk; defer |
| ToggleSprint | Partial only | Movement/input/player capability behavior is sensitive and version-specific | Input/movement adapter | Defer; PvP legitimacy sensitive |
| Crosshair | Partial only | Overlay render and GL state are sensitive; player motion/input checks need validation | Overlay/input adapter | Defer |
| Reach Display | No | Entity/raycast/attack event logic is version-specific and cheat-boundary sensitive | Entity/event adapter | Defer |
| Combo Display | No | Attack/damage events, entity state, and hurt timing are version-specific | Entity/event adapter | Defer |
| Fullbright | Partial | Gamma setting exists but original value restore and lifecycle must be verified | Game settings adapter | Medium risk; after HUD basics |
| Time Changer | Partial | World time access and restore lifecycle are version-specific | World adapter | Medium risk; after HUD basics |
| Carbon Menu | Partial | `GuiScreen`, input, scrolling, color picker, save triggers, and layout need 1.7.10 validation | GUI/input adapter | Defer until module/settings parity exists |
| HUD Editor | Partial | `GuiScreen`, mouse drag, scale, bounds, and save behavior need validation | GUI/input adapter | Defer until draggable HUD modules exist |
| Config/Profile | Mostly yes | Pure serialization is portable, but module snapshots depend on ported module names/settings | Module snapshot adapter | Port after module base/settings |
| Settings System | Mostly yes | `KeybindSetting` depends on LWJGL keyboard constants, but core settings are pure Java | Small keybind adapter | Good early common candidate |

## System Notes

### FPS Display

The 1.8.9 class is a strong direct-port source for settings, default position, style modes, padding, colors, scale, background, HUD bounds, and fallback render shape. The only confirmed mismatch is FPS data access: 1.8.9 uses `Minecraft.getDebugFPS()`, while the 1.7.10 experimental prototype currently uses a local counter because the equivalent field is not public through the same API.

### Clock HUD

Clock is the safest parity target because it uses local/system time and does not need player/world/network data. The main adaptation is time formatting. The 1.8.9 class uses `java.time`; 1.7.10 ForgeGradle source compatibility should avoid relying on Java 8-only APIs in shared code.

### CPS Display

CPS logic should be copied closely: LMB only, timestamp deque, last `1000 ms`, and `N CPS` format. The event source is the risk. The final port should use a 1.7.10-safe mouse event/input adapter rather than adding RMB, total CPS, or any automated behavior.

### Keystrokes

Keystrokes should preserve the 1.8.9 layout, colors, pressed state behavior, show space option, style modes, and bounds. It is not a first direct-port target because input state is hot-path and PvP-feel sensitive.

### Ping Display

Ping rendering and settings can be copied, but data access needs research. 1.8.9 `NetworkPlayerInfo` access is not expected to be a direct 1.7.10 copy.

### Coordinates HUD

Rendering/settings/layout can mostly copy, but player/world/biome data must adapt. 1.8.9 `BlockPos` use is a clear 1.7.10 mismatch.

### Armor and Potion HUD

These should not be early direct-port targets. They involve item/effect rendering, texture state, overlay behavior, durability/icon handling, and GL state. First pass may copy settings and text/background patterns only; item/icon render should stay version-specific.

### Combat-Sensitive Displays

Reach and Combo are deferred. They touch entity, raycast, attack, damage, and timing logic. Porting them before an event/entity adapter would increase regression and cheat-boundary risk.

## Recommended Direct-Port Order

1. Module base, category, settings, and lightweight event model review.
2. Clock HUD direct-port parity pass.
3. FPS Display direct-port parity pass with a 1.7.10-safe FPS source.
4. CPS Display direct-port parity pass with LMB-only input adapter.
5. Draggable HUD position/bounds foundation.
6. Config/profile snapshot compatibility for the ported modules.
7. Keystrokes direct-port pass.
8. Coordinates render/settings port, then world/player/biome adapter.
9. Ping render/settings port, then network data adapter.
10. Carbon Menu and HUD Editor after module/settings/config foundations are stable.

Deferred until later:

- Armor HUD
- Potion HUD
- ToggleSprint
- Crosshair
- Reach Display
- Combo Display
- Fullbright
- Time Changer

## First Recommended Port Candidate

Clock HUD is the first recommended true direct-port candidate.

Reason:

- It has the smallest Minecraft runtime dependency surface.
- It is primarily text/panel rendering plus local time.
- It is ideal for proving module/settings/render parity without world, entity, network, or input complexity.

The second candidate should be FPS Display, followed by CPS Display.

## Build and Branch Boundaries

- Do not change `build.gradle` for this checkpoint.
- Do not switch to main.
- Do not commit or push from this checkpoint.
- Do not modify 1.8.9 sources while preparing this strategy.
- Do not port runtime code as part of this checkpoint.

## Performance and PvP Boundaries

- No per-frame disk IO.
- No per-frame logs.
- No heavy reflection in render/input hot paths.
- No autoclicker, reach, hitbox, velocity, aim assist, packet manipulation, or rod exploit.
- Input-sensitive systems must preserve low latency and legitimate client behavior.
