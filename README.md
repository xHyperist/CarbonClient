# Carbon Client

Carbon Client, Minecraft 1.8.9 Forge uzerine gelistirilmis premium PvP/HUD client altyapisidir. v0.5.16, Keystrokes partial render bridge prototype asamasidir.

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
build/libs/carbon-client-0.5.16.jar
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
5. Jar ile test etmek icin `build/libs/carbon-client-0.5.16.jar` dosyasini Minecraft 1.8.9 Forge `mods` klasorune koy.

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
- Current runtime still remains Minecraft 1.8.9 Forge and does not use the bridge yet.
- This does not mean 1.7.10 support is active yet.
- No 1.12.2, 1.16.5, or 1.20.x target is planned right now.
- See `MULTI_VERSION_ARCHITECTURE_PLAN.md` for common/version-specific system boundaries and migration phases.
- See `BRIDGE_DEPENDENCY_MAP_v0.5.2.md` for class-level dependency categories, bridge needs, and migration order.

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
- v0.6.x: 1.7.10 first boot ve feature parity
- Later / research required: Scoreboard customization after safe vanilla render hook research
- Later / research required: Zoom after OptiFine/launcher compatibility planning
- Later: Block Overlay
- Later: Chat Mod
- Later: Weather Changer
- Sonraki asama: Carbon Launcher ve version selector
