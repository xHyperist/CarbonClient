# Isolated 1.7.10 Branch Preparation v0.6.3

## 1. Purpose

This document prepares the manual steps and safety rules for a future isolated Minecraft 1.7.10 experimental branch.

v0.6.3 is planning-only. It does not create a branch, does not start the 1.7.10 port, does not add dependencies, does not change `build.gradle`, and does not change runtime behavior.

The goal is to protect the stable 1.8.9 runtime while making the first 1.7.10 experiment deliberate, reversible, and narrow.

## 2. Current Stable Runtime

Current stable state:

- Runtime: Minecraft Forge 1.8.9.
- Version: 0.6.3.
- Build system: existing 1.8.9 ForgeGradle setup.
- `build.gradle`: still stable 1.8.9 only.
- Source layout: current `src/main/java`, no source-set split.
- Multi-project Gradle: not enabled.
- 1.7.10 implementation: not started.

Current render-only bridge consumers:

- FPS Display.
- CPS Display.
- Clock HUD.
- Coordinates HUD.
- Keystrokes.
- Ping Display.

Current multi-version status:

- Bridge API skeleton is present.
- Passive 1.8.9 bridge implementation is present.
- `RenderBridgeAccess` helper is present.
- 1.7.10 bridge implementation does not exist yet.

## 3. Why an Isolated Branch Is Required

1.7.10 work has high build and runtime risk because ForgeGradle, MCP mappings, Forge events, GUI/render APIs, entity/world access, item/potion APIs, and input behavior differ from 1.8.9.

The first experiment should not happen on the stable main branch. A failed 1.7.10 build should not block current 1.8.9 development or releases.

An isolated branch keeps the blast radius low and gives the team a clean rollback path.

## 4. Recommended Branch Name

Recommended branch name:

- `experimental/1.7.10-runtime`

Alternative:

- `v0.7-experimental-1.7.10`

Recommendation:

- Use `experimental/1.7.10-runtime` for the first manual experiment because it clearly communicates scope and risk.

## 5. Manual Branch Creation Steps

These commands are documentation only. Codex does not run them in v0.6.3.

```powershell
git status
git checkout main
git pull
git checkout -b experimental/1.7.10-runtime
```

Before running those commands manually, make sure all v0.6.x planning changes are committed or intentionally shelved by the user.

## 6. Safety Rules

- `main` should remain the stable 1.8.9 branch.
- Risky `build.gradle` changes must happen only on the experimental branch.
- 1.7.10 dependencies must be tested only on the experimental branch.
- Do not try to port all modules in the first experiment.
- First target is minimal Forge 1.7.10 client bootstrap.
- If the 1.7.10 build fails, return to `main` and keep 1.8.9 stable.
- Do not merge the experimental branch until it has clean build and manual runClient QA.
- Do not merge partial module ports into main.
- Keep all cheat/PvP advantage boundaries intact.

## 7. First 1.7.10 Experiment Scope

First experiment goals:

- Research minimal 1.7.10 Forge environment setup.
- Attempt minimum Carbon bootstrap.
- Check whether preInit/init/postInit style lifecycle can be mapped safely.
- Investigate how the bridge skeleton could receive 1.7.10 implementations later.
- Document every build, mapping, dependency, and lifecycle issue found.

The first successful milestone should be a minimal bootstrapped client, not feature parity.

## 8. What the First Experiment Must Not Do

- Do not port all modules.
- Do not port the full HUD system.
- Do not port Carbon Menu or HUD Editor.
- Do not rewrite Config/Profile systems.
- Do not move Armor/Potion item/icon rendering.
- Do not move Reach/Combo entity/combat logic.
- Do not move ToggleSprint input/movement logic.
- Do not implement performance/PvP tweaks.
- Do not add cheat features.
- Do not merge experimental work into main before verification.

## 9. 1.7.10 Technical Risk List

Known technical risks:

- ForgeGradle / Gradle compatibility.
- JDK 8 compatibility.
- MCP mapping differences.
- Minecraft class/method name differences.
- Forge lifecycle differences.
- `GuiScreen` / render API differences.
- `FontRenderer` differences.
- `ScaledResolution` differences.
- Keyboard/Mouse input differences.
- Entity/world/player access differences.
- `NetworkPlayerInfo` / ping access differences.
- Potion API differences.
- `ItemStack` / `RenderItem` differences.
- GL state differences.
- Fullbright gamma/options differences.
- Time Changer world time differences.
- Event bus differences.
- Build cache / old ForgeGradle issues.

## 10. Performance-First Branch Rule

The experimental branch must keep performance as a first-class goal.

Rules:

- Do not use heavy reflection in hot paths.
- Do not perform version checks every frame.
- Do not save config/profile data every frame.
- Do not log spam every frame.
- Keep render tick allocation low.
- Keep bridge abstraction thin.
- Disabled modules must not create tick/render cost.
- FPS and frame-time stability are primary validation signals.

## 11. PvP / Rod Responsiveness Branch Rule

Carbon Client's PvP goal is legit client feel and performance, not mechanic manipulation.

Rules:

- Rod/right click/item-use delay must not increase.
- Input paths must not become heavy.
- Keystrokes, CPS, and Ping must remain lightweight.
- Debug log spam must not happen during PvP.
- Config/profile saves must not continuously trigger during combat or render.

Forbidden:

- Rod cooldown bypass.
- Packet spam.
- Packet manipulation.
- Auto rod.
- Autoclicker.
- Reach, hitbox, velocity, or aim assist.
- Server-side mechanic manipulation.

## 12. Rollback Plan

If the experimental branch fails:

- Return to `main`.
- Keep the experimental branch until failure notes are captured.
- Document the failure reason in project docs.
- Re-verify stable 1.8.9 clean build.
- Do not move experimental `build.gradle` changes to main.
- Do not merge module ports to main.

Manual rollback commands, for documentation only:

```powershell
git checkout main
git status
.\gradlew.bat clean build
```

## 13. v0.6.4 Recommended Next Step

Recommended next step:

- v0.6.4 - Low-Risk 1.7.10 Port Candidate Review.

Candidate modules/systems to review:

- Clock HUD.
- FPS Display.
- CPS Display.
- Keystrokes.
- Ping Display.
- Coordinates HUD.

v0.6.4 does not have to port anything. A careful candidate review is still useful before or alongside the first experimental branch.

v0.6.4 follow-up:

- `LOW_RISK_1_7_10_PORT_CANDIDATE_REVIEW_v0.6.4.md` now documents the candidate review.
- Recommended first real module order after minimal bootstrap:
  1. Clock HUD.
  2. FPS Display.
  3. CPS Display.
- Keystrokes, Ping Display, and Coordinates HUD should wait for input/network/world data review.
- Armor HUD, Potion HUD, ToggleSprint, Crosshair, Reach Display, Combo Display, Fullbright, and Time Changer remain deferred from the first experiment.

v0.6.5 follow-up:

- `FIRST_1_7_10_RUNTIME_EXPERIMENT_READINESS_v0.6.5.md` now documents the final pre-branch readiness checklist.
- The first experiment remains minimal Forge 1.7.10 bootstrap only.
- Branch creation remains a manual user action.

## 14. What We Are Not Doing Yet

- Not creating a branch automatically.
- Not starting the 1.7.10 port.
- Not adding 1.7.10 dependencies.
- Not changing `build.gradle`.
- Not creating source sets or multi-project structure.
- Not physically moving packages.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not changing runtime behavior.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
