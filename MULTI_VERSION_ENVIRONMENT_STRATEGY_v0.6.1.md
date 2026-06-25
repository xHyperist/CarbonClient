# 1.7.10 Environment Strategy v0.6.1

## 1. Purpose

This document decides the safest first strategy for future Minecraft 1.7.10 work.

v0.6.1 is planning-only. It does not create a branch, does not change `build.gradle`, does not add 1.7.10 dependencies, does not add source sets, and does not start the 1.7.10 port.

The goal is to protect the stable 1.8.9 runtime while defining a practical path for the first isolated 1.7.10 experiment.

## 2. Current Stable Runtime

The current project remains a single Minecraft 1.8.9 Forge runtime:

- `build.gradle` uses ForgeGradle 2.1 and one `sourceSets.main` runtime.
- `gradle.properties` targets Minecraft 1.8.9 and Forge `11.15.1.2318`.
- `settings.gradle` only defines the root project.
- Source currently lives under `src/main/java`.
- Bridge packages exist under `com.carbonclient.bridge`.
- Render-only bridge consumers remain FPS Display, CPS Display, Clock HUD, Coordinates HUD, Keystrokes, and Ping Display.
- Most runtime, GUI, event, input, entity, world, item, potion, and GL code remains direct 1.8.9.

The stable 1.8.9 branch must continue to build and run while multi-version work is planned.

## 3. Why 1.7.10 Needs Isolation

Minecraft 1.7.10 introduces risk because ForgeGradle, MCP mappings, Forge event behavior, render APIs, input handling, item/potion APIs, and entity/world access differ from 1.8.9.

Changing the main 1.8.9 build too early would risk breaking the currently stable client before we know the 1.7.10 environment can even bootstrap.

The first 1.7.10 work should therefore be isolated from the stable 1.8.9 runtime.

## 4. Option A - Separate Branch Strategy

Example:

- `main` or `main-1.8.9`: stable 1.8.9.
- `experimental-1.7.10`: isolated experiment.

Pros:

- Safest first step.
- Stable 1.8.9 branch remains protected.
- Old ForgeGradle conflicts do not affect main.
- Easy rollback if the 1.7.10 environment fails.
- Lowest user-facing risk.

Cons:

- Common code can drift between branches.
- Code duplication can increase.
- Long-term two-branch maintenance can become noisy.

Assessment:

This is the best fit for Carbon Client's current stage. The project has a stable 1.8.9 runtime, a single Gradle build, and many direct Minecraft/Forge calls. Before introducing source-set or multi-project complexity, we should prove that a minimal 1.7.10 environment can bootstrap in isolation.

## 5. Option B - Source Set Strategy

Example:

- `src/common/java`
- `src/v1_8_9/java`
- `src/v1_7_10/java`

Pros:

- Cleaner architecture.
- Stronger shared common code story.
- Better long-term readability than branch-only duplication.

Cons:

- Risky with old ForgeGradle.
- Adds `build.gradle` complexity.
- Can break the stable 1.8.9 build early.
- Separating 1.8.9 and 1.7.10 dependencies in one old ForgeGradle project may be awkward.

Assessment:

This is not ready for immediate implementation. It may become useful after an isolated 1.7.10 branch proves the environment and after v0.6.2 defines common vs version-specific package separation in detail.

## 6. Option C - Multi-Project Gradle Strategy

Example:

- `carbon-common`
- `carbon-1.8.9`
- `carbon-1.7.10`

Pros:

- Most professional long-term structure.
- Clear runtime separation.
- Common code can be shared intentionally.

Cons:

- Highest initial setup risk.
- Old ForgeGradle compatibility issues are likely.
- Build complexity increases sharply.
- Too much risk for the stable client at the current stage.

Assessment:

This may be ideal later, but it is too early for v0.6.1. It should remain a long-term architecture option, not the first 1.7.10 experiment.

## 7. Recommendation

Recommended strategy for v0.6.x:

- Use Option A: isolated experimental 1.7.10 branch for the first environment test.
- Keep the stable 1.8.9 main branch protected.
- Do not change `build.gradle` on the stable branch yet.
- Do not create source sets or multi-project Gradle structure in v0.6.1.
- First prove whether a minimal 1.7.10 Forge environment can bootstrap.
- If the experiment succeeds, revisit Option B or Option C later with real data.

Reason:

Option A has the lowest blast radius. It lets Carbon Client learn about 1.7.10 ForgeGradle and MCP problems without endangering the working 1.8.9 build.

## 8. Performance-First Requirement

The multi-version strategy must not reduce FPS.

Requirements:

- Bridge abstraction must not become excessively layered.
- Do not use heavy reflection in render tick.
- Do not perform per-frame version checks in hot paths.
- Avoid unnecessary allocation in frequently called code.
- Do not run config/profile IO in render/tick loops.
- Disabled modules should not create tick/render cost.
- First 1.7.10 experiments should include benchmark and frame-time observation plans.
- Any future common abstraction must be measured against frame time stability.

## 9. PvP / Rod Responsiveness Requirement

Carbon Client must preserve low input delay and smooth PvP feel.

Requirements:

- Fishing rod, right click, and item use should feel low-delay.
- Keystrokes, CPS, Ping, and HUD modules should stay lightweight during PvP.
- HUD rendering must not create frame spikes.
- Debug logging must not spam during PvP.
- Config/profile saving must not continuously trigger during PvP.

Strict boundaries:

- No rod cooldown bypass.
- No packet spam.
- No packet manipulation.
- No auto rod.
- No autoclicker.
- No reach, hitbox, velocity, or aim assist.
- No server-side mechanic manipulation.

This is a performance and input responsiveness goal, not a cheat feature goal.

## 10. 1.7.10 First Experiment Entry Criteria

Before creating the first 1.7.10 experimental branch:

- 1.8.9 main branch clean build is successful.
- v0.5.23 persistence hotfix has been manually validated by the user at a reasonable level.
- v0.6.1 strategy document is complete.
- 1.7.10 ForgeGradle and JDK requirements are researched.
- First 1.7.10 work happens on an isolated branch.
- Stable 1.8.9 branch does not receive risky `build.gradle` changes.
- First port attempt targets minimal client bootstrap only.
- HUD/module porting is not forced in the first experiment.
- Performance-first rule remains active.

## 11. Risks

- Branch drift between 1.8.9 and 1.7.10 experiments.
- Common code duplication if branch isolation lasts too long.
- Old ForgeGradle compatibility issues.
- MCP mapping mismatch.
- Forge event differences.
- Input/keybind behavior differences.
- GL/render state differences.
- Item/potion API differences.
- Ping/network info differences.
- Performance regressions if abstractions are too heavy.

## 12. v0.6.2 Recommended Next Step

Recommended next step:

- v0.6.2 - Common vs Version-Specific Package Separation Plan.

The next planning phase should define which packages can stay common, which must remain version-specific, and which need adapters before any 1.7.10 branch experiment begins.

v0.6.2 follow-up:

- `COMMON_VERSION_SPECIFIC_SEPARATION_PLAN_v0.6.2.md` now documents that classification.
- No branch was created and no `build.gradle` change was made in v0.6.2.

## 13. What We Are Not Doing Yet

- Not starting the 1.7.10 port.
- Not adding 1.7.10 dependencies.
- Not changing `build.gradle`.
- Not creating source sets.
- Not creating a multi-project build.
- Not creating a branch automatically.
- Not adding a new module.
- Not adding a new bridge consumer.
- Not changing runtime behavior.
- Not starting launcher/auth/cosmetic work.
- Not adding cheat/PvP advantage features.
