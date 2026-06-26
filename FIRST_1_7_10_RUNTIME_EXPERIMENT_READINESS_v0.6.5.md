# First 1.7.10 Runtime Experiment Readiness v0.6.5

## 1. Purpose

This document is the final readiness checklist before the user manually creates the first isolated Minecraft 1.7.10 experimental branch.

v0.6.5 is planning-only. It does not create a branch, does not start the 1.7.10 port, does not add dependencies, does not change `build.gradle`, and does not change runtime code.

## 2. Stable Main Protection

- The stable main branch must remain Minecraft Forge 1.8.9.
- The current 1.8.9 build must stay clean before and after any future experimental work.
- No 1.7.10 dependency should be added to main.
- No risky `build.gradle` change should be made on main.
- No package move, source-set split, or multi-project layout should happen on main for this readiness step.
- Experimental failures must not block the stable 1.8.9 runtime.

## 3. Experimental Branch Name

Recommended branch name:

- `experimental/1.7.10-runtime`

This branch should be created manually by the user when ready. Codex does not create it in v0.6.5.

## 4. First Experiment Scope

The first experiment target is only minimal Forge 1.7.10 bootstrap.

Allowed first experiment goals:

- Research Forge 1.7.10 environment setup.
- Attempt a minimal client bootstrap.
- Validate basic client lifecycle init.
- Check whether a small 1.7.10 bridge skeleton can be introduced safely.
- Keep logging minimal and startup-focused.

Not in the first experiment:

- No HUD/module port.
- No Carbon Menu port.
- No HUD Editor port.
- No config/profile rewrite.
- No RenderUtils migration.
- No Armor/Potion item/icon render migration.
- No ToggleSprint, Reach, Combo, Crosshair, Fullbright, or Time Changer migration.
- No performance/PvP tweak implementation.
- No cheat or PvP advantage feature.

## 5. Branch-Only Build Rules

- Any 1.7.10 `build.gradle` experiment must happen only on `experimental/1.7.10-runtime`.
- Main must not receive 1.7.10 dependencies.
- Main must not receive 1.7.10 mappings.
- Main must not receive source-set or multi-project Gradle changes during this step.
- Before returning to main work, verify main with `.\gradlew.bat clean build`.

## 6. Success Criteria

The first isolated 1.7.10 runtime experiment can be considered successful only if:

- The experimental branch attempts a 1.7.10 build without modifying stable main.
- Minimal client init is reached or the blocker is clearly documented.
- Startup is crash-free for the minimal bootstrap target, or crash cause is documented.
- Main 1.8.9 `.\gradlew.bat clean build` remains successful after the experiment.
- No HUD/module port is required for the first success milestone.
- No cheat/PvP advantage behavior is introduced.

## 7. Risk List

Expected risks:

- ForgeGradle compatibility with older Minecraft/Forge versions.
- JDK 8 requirements.
- MCP mapping differences.
- Forge lifecycle differences.
- GUI/render API differences.
- FontRenderer and ScaledResolution differences.
- Keyboard/mouse input differences.
- Entity, world, and player access differences.
- Network/player info differences.
- Potion and item rendering API differences.
- GL state differences.
- Old build cache and userdev setup issues.

## 8. Rollback Plan

If the experimental branch fails:

- Do not merge the failed experiment into main.
- Keep notes about the failure before deleting or abandoning the branch.
- Return to main.
- Verify main status.
- Re-run `.\gradlew.bat clean build` on main.
- Do not copy risky 1.7.10 `build.gradle` changes into main.
- Do not move partial module ports into main.

Manual rollback commands for documentation only:

```powershell
git checkout main
git status
.\gradlew.bat clean build
```

## 9. Performance-First Rule

The first 1.7.10 experiment must preserve Carbon Client's high-FPS / performance-first goal.

Rules:

- No heavy abstraction in hot paths.
- No render tick disk IO.
- No render tick log spam.
- No render tick allocation spikes.
- No per-frame version lookup.
- No disabled-module tick/render overhead.
- Keep bridge abstraction thin and direct.
- Record FPS/frame-time observations once a minimal runtime can launch.

## 10. PvP / Rod Responsiveness Rule

The first experiment must not increase input delay or change server-side mechanics.

Rules:

- Input path must stay lightweight.
- Rod/right click/item-use feel must not be delayed by Carbon systems.
- Keystrokes/CPS/Ping should remain lightweight when eventually tested.
- Config/profile save must not run continuously during combat/render.
- Debug logs must not spam during PvP.

Forbidden:

- Rod cooldown bypass.
- Packet spam.
- Packet manipulation.
- Auto rod.
- Autoclicker.
- Reach/hitbox/velocity/aim assist.
- Server-side mechanic manipulation.

## 11. Pre-Branch Checklist

- [ ] Main branch is clean or intentionally committed/stashed by the user.
- [ ] Main is confirmed as the stable 1.8.9 branch.
- [ ] `.\gradlew.bat clean build` passes on main.
- [ ] v0.6.6 final 1.8.9 pre-branch stability checkpoint is reviewed.
- [ ] User manually creates `experimental/1.7.10-runtime` only when ready.
- [ ] First experiment scope is limited to minimal Forge 1.7.10 bootstrap.
- [ ] No HUD/module port is attempted in the first experiment.
- [ ] 1.7.10 dependency changes stay on the experimental branch.
- [ ] Rollback plan is understood.
- [ ] Performance-first rule is active.
- [ ] PvP/rod responsiveness rule is active.

v0.6.6 follow-up:

- `FINAL_1_8_9_PRE_BRANCH_STABILITY_CHECKPOINT_v0.6.6.md` now documents the final 1.8.9 clean build and manual QA checkpoint before branch creation.
- Branch creation remains a manual user action.
- The first experimental branch remains minimal Forge 1.7.10 bootstrap only.

## 12. What We Are Not Doing Yet

- Not creating a branch.
- Not starting the 1.7.10 port.
- Not adding 1.7.10 dependencies.
- Not changing `build.gradle`.
- Not moving packages.
- Not changing runtime code.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
