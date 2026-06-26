# Carbon Client

Carbon Client, Minecraft 1.8.9 Forge uzerine gelistirilmis premium PvP/HUD client altyapisidir. v0.6.6, Final 1.8.9 Pre-Branch Stability Checkpoint asamasidir.

Carbon Client bir cheat/hile client degildir. Fly, speed, reach, velocity, autoclicker, aim assist veya combat avantaji saglayan ozellikler hedeflenmez.

## Hedef Surumler

- Ana hedef: Minecraft 1.8.9 Forge
- Gelecek ikinci hedef: Minecraft 1.7.10
- Su an hedeflenmeyen surumler: 1.12.2, 1.16.5, 1.20.x

## Kurulum ve Gelistirme

Gerekenler:

- Java 8
- Minecraft Forge 1.8.9 / ForgeGradle 2.x
- Windows icin Gradle wrapper: `gradlew.bat`

Gelistirme client'ini baslatmak:

```powershell
.\gradlew.bat runClient
```

Jar uretmek:

```powershell
.\gradlew.bat build
```

Cikti jar dosyasi:

```text
build/libs/carbon-client-0.6.6.jar
```

Temiz build almak:

```powershell
.\gradlew.bat clean build
```

## Mevcut Ozellikler

- Module system ve ModuleManager
- Lightweight EventBus
- Setting system: Boolean, Number, Mode, Color, Keybind
- Config save/load, defaults ve reset sistemi
- Profiles system
- Notification / Toast system
- Carbon Menu
- Visuals tab, Fullbright ve Time Changer quick settings
- Mods Tab search, scroll ve category filters
- Options screen
- Keybinds panel ve conflict warning
- Profesyonel Color Picker
- HUD Layout Editor
- FPS Display
- CPS Display
- Keystrokes
- ToggleSprint / ToggleSneak
- Armor HUD
- Potion HUD
- Coordinates HUD
- Ping Display
- Reach Display
- Combo Display
- Clock HUD
- Crosshair Editor ve preview

## Auth / Account Karari

- Carbon register sistemi olmayacak.
- Ileride launcher tarafinda Premium Minecraft Account baglantisi olacak.
- Guest Mode olacak.
- Premium hesap baglantisi Microsoft/Mojang uyumlu resmi hesap akisi mantigiyla planlanacak.
- Rank ve cosmetic sistemleri bu auth yapisina gore ileride kurgulanacak.

## Projeyi Nasil Takip Ederim?

Bu asamada proje launcher degil, Forge 1.8.9 mod/client olarak calisir. Ana gelistirme akisi `runClient` ile test etmek ve `build` ile jar uretmektir.

Onemli dosyalar:

- `Client.java`: client servislerini, modulleri ve lifecycle akislarini baslatir.
- `Module.java`: tum modullerin temel sinifidir.
- `ModuleManager.java`: modul kayit, toggle ve lookup islemlerini yonetir.
- `ConfigManager.java`: local config save/load/default/reset akisini yonetir.
- `ProfileManager.java`: local profil olusturma, yukleme, kaydetme ve kopyalama islemlerini yonetir.
- `CarbonMenuScreen.java`: Carbon Menu, Mods Tab, Options, Keybinds ve Profiles ekranlarini icerir.
- `HudLayoutEditorScreen.java`: HUD modullerinin ekrandaki konumlarini duzenler.

## Carbon Client Su An Nasil Test Edilir?

1. `.\gradlew.bat runClient` ile gelistirme client'ini ac.
2. Minecraft ana menusu acildiginda Carbon Client loglarini kontrol et.
3. Oyun icinde `RSHIFT` ile Carbon Menu'yu ac.
4. Mods Tab, Options, Keybinds, Profiles, Color Picker ve HUD Editor akislarini test et.
5. Jar ile test etmek icin `build/libs/carbon-client-0.6.6.jar` dosyasini Minecraft 1.8.9 Forge `mods` klasorune koy.

## Multi-Version Plan

- Current main version: Minecraft 1.8.9 Forge
- Next target: Minecraft 1.7.10
- v0.5.0 starts architecture planning only; it does not start actual 1.7.10 coding.
- v0.5.1 adds a safe bridge API skeleton under `com.carbonclient.bridge`.
- v0.5.2 adds bridge dependency mapping for current 1.8.9 Minecraft/Forge dependencies.
- v0.5.3 adds passive 1.8.9 Game/Input/Render bridge implementations and registers them for future migration.
- v0.5.4 verifies passive bridge runtime safety; modules still use the direct 1.8.9 implementation.
- v0.5.5 adds internal bridge diagnostics; this is not a user-facing feature.
- v0.5.6 lets FPS Display try RenderBridge internally while keeping legacy fallback and unchanged user behavior.
- v0.5.7 QA-checks FPS bridge consumer behavior and keeps fallback mandatory.
- v0.5.8 lets CPS Display try RenderBridge internally while keeping LMB-only counting and legacy fallback.
- v0.5.9 QA-checks CPS bridge consumer behavior, LMB-only counting, and legacy fallback.
- v0.5.10 lets Clock HUD try RenderBridge internally while keeping local/system-time formatting and legacy fallback.
- v0.5.11 QA-checks Clock HUD bridge consumer behavior, local/system-time formatting, and legacy fallback.
- v0.5.12 QA-checks FPS, CPS, and Clock HUD bridge-assisted consumers together.
- v0.5.13 lets Coordinates HUD try RenderBridge internally while keeping player/world data access on the legacy 1.8.9 path.
- v0.5.14 QA-checks Coordinates HUD bridge consumer behavior while keeping data access direct 1.8.9.
- v0.5.15 QA-checks FPS, CPS, Clock, and Coordinates render bridge consumers together.
- v0.5.16 lets Keystrokes try RenderBridge internally for rendering only, while key/mouse state remains legacy direct 1.8.9.
- v0.5.17 QA-checks Keystrokes bridge-assisted render, legacy fallback, and direct key/mouse state behavior.
- v0.5.18 QA-checks FPS, CPS, Clock, Coordinates, and Keystrokes render bridge consumers together.
- v0.5.19 extracts a small helper for safe RenderBridge access and metric handling without user-facing behavior changes.
- v0.5.20 QA-checks the RenderBridge helper and existing FPS/CPS/Clock/Coordinates/Keystrokes consumers.
- v0.5.21 lets Ping Display try RenderBridge internally for rendering only, while ping data access remains direct 1.8.9.
- v0.5.22 QA-checks Ping Display bridge-assisted render while keeping ping data access direct 1.8.9.
- v0.5.23 fixes and validates module enabled-state persistence across restarts, and adds Armor/Potion bridge risk analysis.
- v0.5.23-hotfix audits the real file state and prevents stale active profile snapshots from overwriting newer config enabled states.
- v0.5.24 completes the Multi-Version Bridge Phase Review, reviews v0.5.x render bridge preparation, and prepares v0.6.0 planning.
- v0.6.0 starts Multi-Version Runtime Preparation, documents performance-first and PvP responsiveness goals, and keeps runtime on 1.8.9 with no 1.7.10 code/dependency yet.
- v0.6.1 documents the 1.7.10 environment strategy, compares separate branch/source-set/multi-project approaches, and recommends an isolated branch for the first 1.7.10 experiment.
- v0.6.2 documents the common vs version-specific package separation plan without moving packages or changing runtime behavior.
- v0.6.3 documents isolated 1.7.10 experimental branch preparation, manual branch steps, safety rules, and rollback planning without creating a branch.
- v0.6.4 reviews low-risk 1.7.10 port candidates, identifies Clock/FPS/CPS as the safest early candidates, defers high-risk modules, keeps the current runtime on 1.8.9, and adds no 1.7.10 code/dependency.
- v0.6.5 adds the final readiness checklist before the user manually creates the isolated `experimental/1.7.10-runtime` branch.
- v0.6.6 adds the final 1.8.9 pre-branch stability checkpoint and manual QA checklist before any 1.7.10 branch work.
- Current runtime still remains Minecraft 1.8.9 Forge; bridge usage is limited to internal render-assisted consumers.
- This does not mean 1.7.10 support is active yet.
- No 1.12.2, 1.16.5, or 1.20.x target is planned right now.
- See `MULTI_VERSION_ARCHITECTURE_PLAN.md` for common/version-specific system boundaries and migration phases.
- See `BRIDGE_DEPENDENCY_MAP_v0.5.2.md` for class-level dependency categories, bridge needs, and migration order.
- See `MULTI_VERSION_PHASE_REVIEW_v0.5.24.md` for the v0.5.x bridge checkpoint and v0.6.x roadmap.
- See `MULTI_VERSION_RUNTIME_PREPARATION_v0.6.0.md` for the v0.6 runtime preparation, performance-first rule, and PvP responsiveness plan.
- See `MULTI_VERSION_ENVIRONMENT_STRATEGY_v0.6.1.md` for the 1.7.10 environment strategy decision.
- See `COMMON_VERSION_SPECIFIC_SEPARATION_PLAN_v0.6.2.md` for common/version-specific package classification and adapter needs.
- See `ISOLATED_1_7_10_BRANCH_PREPARATION_v0.6.3.md` for manual experimental branch preparation and rollback rules.
- See `LOW_RISK_1_7_10_PORT_CANDIDATE_REVIEW_v0.6.4.md` for the first 1.7.10 port candidate risk review.
- See `FIRST_1_7_10_RUNTIME_EXPERIMENT_READINESS_v0.6.5.md` for the final pre-branch readiness checklist.
- See `FINAL_1_8_9_PRE_BRANCH_STABILITY_CHECKPOINT_v0.6.6.md` for the final 1.8.9 stability checkpoint before branch creation.

## Yasak / Istenmeyen Yon

Carbon Client bir cheat/hile client degildir. Asagidaki ozellikler hedeflenmez:

- Fly
- Speed
- Reach
- Velocity
- Autoclicker
- Aim assist
- Combat avantaji saglayan hile sistemleri

## Roadmap

- v0.4.x: 1.8.9 polish, stabilite ve UX iyilestirmeleri
- v0.5.x: Multi-version architecture planning ve 1.7.10 port hazirligi
- v0.6.x: Multi-version runtime preparation and isolated 1.7.10 strategy
- v0.8.x: Performance / FPS Optimization Phase
- Later / research required: Scoreboard customization after safe vanilla render hook research
- Later / research required: Zoom after OptiFine/launcher compatibility planning
- Later: Block Overlay
- Later: Chat Mod
- Later: Weather Changer
- Sonraki asama: Carbon Launcher ve version selector
