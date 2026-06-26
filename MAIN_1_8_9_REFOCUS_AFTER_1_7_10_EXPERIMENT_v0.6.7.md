# Main 1.8.9 Refocus After 1.7.10 Experiment v0.6.7

## Purpose

This checkpoint pauses the isolated 1.7.10 experiment and refocuses Carbon Client development on the stable main 1.8.9 runtime.

The 1.7.10 experiment proved enough technical ground to keep the branch valuable, but the main product should now continue maturing on 1.8.9 before more parity work is ported.

## What Was Proven on `experimental/1.7.10-runtime`

- A minimal Minecraft Forge 1.7.10 bootstrap is technically possible.
- The 1.7.10 client can launch through `runClient`.
- The legacy ForgeGradle assets index workaround works well enough for local runtime testing.
- Minimal Clock HUD, FPS Display, and CPS Display prototypes can render on the 1.7.10 overlay path.
- A shared experimental HUD renderer can approximate the Carbon 1.8.9 HUD panel language.
- A direct-port strategy was defined: 1.7.10 should target 1.8.9 design and behavior parity instead of drifting into a separate UX.

## Pause Decision

The 1.7.10 experiment is paused for now.

Reasons:

- The branch has proven the core environment and low-risk HUD rendering path.
- The main 1.8.9 runtime is still the active product branch.
- Continuing 1.7.10 porting too early would duplicate work before 1.8.9 systems are fully mature.
- Carbon Menu, HUD Editor, config/profile, module settings, and higher-risk HUDs should be stabilized further on 1.8.9 first.
- Future 1.7.10 work should port from a stronger 1.8.9 baseline.

## Main Branch Policy

- `main` remains the stable Minecraft Forge 1.8.9 runtime.
- `experimental/1.7.10-runtime` is preserved as a separate experimental branch.
- 1.7.10 code and dependency changes should not be merged into `main` yet.
- `build.gradle` stays on the 1.8.9 setup in `main`.
- Runtime behavior is not changed by this checkpoint.

## Future 1.7.10 Rule

When 1.7.10 work resumes, it should follow the direct-port parity rule:

- Inspect the 1.8.9 class first.
- Copy shared behavior, settings, layout, colors, spacing, and formatting where possible.
- Adapt only the version-specific Minecraft/Forge API calls.
- Do not introduce 1.7.10-only alternate design or behavior.
- Do not add cheat/PvP advantage features.

## Main 1.8.9 Refocus

The main development focus returns to 1.8.9:

- Improve and stabilize the existing 1.8.9 module system.
- Continue refining Carbon Menu, HUD Editor, settings, config/profile, and visual systems.
- Keep performance-first and PvP responsiveness boundaries.
- Build the best possible 1.8.9 Carbon Client before broadening parity work.

## Recommended Next Stage

`v0.7.0 - Carbon Client 1.8.9 Roadmap Refresh`

Recommended focus:

- Re-evaluate the 1.8.9 product roadmap.
- Prioritize user-facing polish and stability.
- Review HUD/menu/profile pain points.
- Decide the next safe 1.8.9 feature or QA milestone.
- Keep 1.7.10 paused until the 1.8.9 baseline is stronger.

## What We Are Not Doing

- No 1.7.10 code is merged into main.
- No `build.gradle` change is made.
- No new module is added.
- No runtime behavior is changed.
- No new dependency is added.
- No cheat/PvP advantage feature is added.
