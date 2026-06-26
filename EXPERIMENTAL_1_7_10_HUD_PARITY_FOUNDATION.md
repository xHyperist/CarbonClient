# Experimental 1.7.10 HUD Parity Foundation

## Purpose

This document records the first visual parity foundation for the experimental 1.7.10 HUD prototypes.

The final target is strict user-facing parity with the 1.8.9 Carbon Client HUD modules. A user switching Minecraft versions should feel that only the Minecraft runtime changed, not the Carbon HUD design language.

## What Changed

- Added a small shared experimental HUD renderer for text panels.
- Updated the 1.7.10 Clock HUD prototype to use Carbon-style panel rendering.
- Updated the 1.7.10 FPS Display prototype to use Carbon-style panel rendering.
- Preserved the minimal experimental bootstrap scope.

## Visual Parity Direction

- FPS Display now mirrors the 1.8.9 Modern-style defaults more closely:
  - Position near `x=5`, `y=0`.
  - White text.
  - Dark translucent background.
  - Padding of `4`.
  - Scale of `1.0`.
- Clock HUD now mirrors the 1.8.9 Clock defaults more closely:
  - Position near `x=5`, `y=108`.
  - White text.
  - Dark translucent background.
  - Padding of `3`.
  - Scale of `1.0`.

## Current Limits

- Config is not ported yet.
- Profile persistence is not ported yet.
- Carbon Menu is not ported yet.
- HUD Editor is not ported yet.
- User settings for scale, color, style, and position are not ported yet.
- The 1.7.10 FPS prototype still uses a lightweight local frame counter until a final parity-safe FPS source is selected.

## Future Rule

Future experimental 1.7.10 HUD prototypes should use the same helper and visual tokens unless a later full module/settings port replaces it with the shared Carbon HUD system.

## Boundaries

- No 1.8.9 source changes.
- No 1.8.9 module system port.
- No new gameplay feature.
- No cheat/PvP advantage feature.
- No disk IO or logging during render.
