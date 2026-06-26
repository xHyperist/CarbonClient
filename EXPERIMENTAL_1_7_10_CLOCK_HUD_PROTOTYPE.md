# Experimental 1.7.10 Clock HUD Prototype

## Purpose

This document records the first minimal HUD prototype on the `experimental/1.7.10-runtime` branch.

The prototype is intentionally tiny. It does not port the 1.8.9 module system, Carbon Menu, config/profile system, HUD Editor, or RenderBridge consumers.

## Scope

- Adds a 1.7.10-only `ClockHudOverlay`.
- Registers it from the minimal experimental `CarbonClient` init lifecycle.
- Uses Forge 1.7.10 `RenderGameOverlayEvent.Text`.
- Draws a small white text string in the top-left corner.
- Example text: `Carbon 1.7.10 | HH:mm:ss`.

## Boundaries

- No HUD/module system port.
- No settings/config/profile persistence.
- No Carbon Menu port.
- No HUD Editor port.
- No disk IO or logging during render.
- No cheat/PvP advantage feature.

## Verification

- `.\gradlew.bat clean build` should remain successful.
- `.\gradlew.bat runClient` should launch the 1.7.10 client.
- In-game overlay should show the simple clock text at the top-left.
