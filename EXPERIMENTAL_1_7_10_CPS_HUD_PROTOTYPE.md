# Experimental 1.7.10 CPS HUD Prototype

## Purpose

This document records the third minimal HUD prototype on the `experimental/1.7.10-runtime` branch.

The final target is strict parity with the 1.8.9 Carbon Client CPS Display design, settings, behavior, and user experience.

## Scope

- Adds a 1.7.10-only `CpsHudOverlay`.
- Registers it from the minimal experimental `CarbonClient` init lifecycle.
- Uses Forge 1.7.10 `RenderGameOverlayEvent.Text`.
- Tracks only left mouse button clicks.
- Counts clicks inside the last `1000 ms`, matching the 1.8.9 CPS window.
- Draws with `ExperimentalHudRenderer` using the same Carbon-style panel language as FPS.
- Example text: `0 CPS`.

## Input Boundary

- Only LMB / mouse button `0` is tracked.
- RMB CPS is not added.
- Total CPS is not added.
- No autoclicker-like behavior is added.
- No packet, combat, reach, or server-side mechanic behavior is changed.

## Positioning

- FPS HUD: top-left at `x=5`, `y=0`.
- CPS HUD: top-left at `x=5`, `y=20`.
- Clock HUD: top-left at `x=5`, `y=108`.
- The three prototype overlays should not overlap.

## Current Limits

- 1.8.9 module system is not ported.
- Carbon Menu is not ported.
- Config/profile persistence is not ported.
- HUD Editor is not ported.
- User settings for style, scale, color, and position are not ported yet.

## Verification

- `.\gradlew.bat clean build` should remain successful.
- `.\gradlew.bat runClient` should launch the 1.7.10 client.
- In-game overlay should show FPS, CPS, and Clock together without overlap.
