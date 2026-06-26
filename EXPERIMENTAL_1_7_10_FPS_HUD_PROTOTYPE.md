# Experimental 1.7.10 FPS HUD Prototype

## Purpose

This document records the second minimal HUD prototype on the `experimental/1.7.10-runtime` branch.

The prototype is intentionally small and temporary. The long-term goal is still parity with the 1.8.9 Carbon Client FPS Display design, settings, behavior, and user experience.

## Scope

- Adds a 1.7.10-only `FpsHudOverlay`.
- Registers it from the minimal experimental `CarbonClient` init lifecycle.
- Uses Forge 1.7.10 `RenderGameOverlayEvent.Text`.
- Uses a lightweight local frame counter because the 1.7.10 debug FPS field is not publicly accessible through the same API as 1.8.9.
- Draws a small white text string under the experimental Clock HUD.
- Example text: `120 FPS`.

## Positioning

- Clock HUD: top-left at `x=4`, `y=4`.
- FPS HUD: top-left at `x=4`, `y=16`.
- The two prototype overlays should not overlap.

## Boundaries

- No 1.8.9 module system port.
- No Carbon Menu port.
- No config/profile system port.
- No HUD Editor port.
- No settings, scaling, color, style, or drag behavior yet.
- No disk IO or logging during render.
- No cheat/PvP advantage feature.

## Verification

- `.\gradlew.bat clean build` should remain successful.
- `.\gradlew.bat runClient` should launch the 1.7.10 client.
- In-game overlay should show the simple FPS text below the simple Clock HUD.
