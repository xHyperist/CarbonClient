# Changelog

## v0.5.7 - FPS Bridge Consumer QA

- Validated FPS Display bridge-assisted render path.
- Confirmed legacy fallback remains in place.
- Adjusted bridge draw behavior so FPS can fall back if bridge drawing throws.
- Confirmed FPS settings, config/profile format, and HUD Editor bounds remain unchanged.
- Confirmed no other module migration.
- Existing 1.8.9 build remains stable.

## v0.5.6 - FPS Display Bridge-Assisted Render Prototype

- Added fallback-safe `RenderBridge` usage in FPS Display.
- Kept the legacy FPS render path.
- Kept FPS Display settings, config/profile format, HUD Editor bounds, and visual behavior unchanged.
- No user-facing render backend setting was added.
- No other modules were migrated to bridge APIs.
- No 1.7.10 code, dependency, source-set, or build.gradle changes were introduced.

## v0.5.5 - Bridge Diagnostics / Internal Validation

- Added `BridgeDiagnostics`.
- Added `BridgeDiagnosticsReport`.
- Added core bridge readiness checks for Game/Input/Render bridge availability.
- Added one init-time diagnostics summary log after passive bridge bootstrap.
- Kept diagnostics internal with no user-facing UI, HUD, command, keybind, or notification.
- No modules were migrated to bridge APIs yet.
- No 1.7.10 code, dependency, source-set, or build.gradle changes were introduced.
- Existing 1.8.9 build remains stable.

## v0.5.4 - Passive Bridge Runtime QA

- Verified passive bridge bootstrap behavior and kept it idempotent.
- Hardened `BridgeRegistry` so null registrations are ignored instead of clearing existing bridges.
- Hardened `V189InputBridge` for uncreated LWJGL keyboard/mouse state.
- Verified Game/Input/Render bridge package boundaries.
- Confirmed no module migration yet.
- Confirmed no 1.7.10 dependency, source-set, or build.gradle changes.
- Existing 1.8.9 build remains stable.

## v0.5.3 - Passive 1.8.9 Bridge Implementation

- Added `V189GameBridge`.
- Added `V189InputBridge`.
- Added `V189RenderBridge`.
- Added `V189BridgeBootstrap`.
- Registered passive Game/Input/Render bridges through `BridgeRegistry`.
- Updated `BridgeVersionInfo` status to `PASSIVE_IMPLEMENTATION`.
- No modules were migrated to bridge APIs yet.
- Existing 1.8.9 build remains stable.

## v0.5.2 - Bridge Documentation + Dependency Mapping

- Added `BRIDGE_DEPENDENCY_MAP_v0.5.2.md`.
- Classified common-ready, adapter-needed, version-specific, and risky port systems.
- Mapped current modules and UI/event systems to future bridge needs.
- Added a migration order for low-risk bridge implementation, HUD migration, UI abstraction, medium-risk modules, and risky modules.
- No runtime bridge migration yet.
- No 1.7.10 code, dependency, source-set, or build.gradle changes were introduced.
- Kept the existing 1.8.9 build stable.

## v0.5.1 - Safe Bridge Preparation

- Added bridge API skeleton for future game, render, input, event, entity, world, and config abstractions.
- Added `BridgeRegistry` as a future coordination point for bridge instances.
- Added 1.8.9 bridge version info placeholder with status `PLANNED`.
- No active runtime migration yet; existing 1.8.9 systems still use the current direct implementation.
- No 1.7.10 code, dependency, source-set, or build.gradle changes were introduced.
- Kept the existing 1.8.9 build stable.

## v0.5.0 - Multi-Version Architecture Planning

- 1.7.10 implementation has not started yet.
- Added `MULTI_VERSION_ARCHITECTURE_PLAN.md`.
- Mapped common systems vs version-specific systems.
- Added compatibility layer proposal for game, render, input, event, entity, world, and config boundaries.
- Added module portability matrix and 1.7.10 port risk notes.
- Added migration phases for safe future bridge work.
- Kept the existing 1.8.9 build stable.

## v0.4.10 - Final 1.8.9 Release Candidate QA

- Yeni ozellik eklenmedi.
- Multi-version architecture planning oncesi final 1.8.9 Release Candidate QA yapildi.
- README, QA checklist ve release notes dokumantasyonu temizlendi.
- Config/profile backward compatibility akisi kontrol edildi.
- HUD modules QA, Visuals QA ve PvP info HUD safety check tamamlandi.
- Scoreboard, BlockOverlay, Chat, Weather, Zoom veya Capture sistemi geri eklenmedi.
- Build verification `clean build` ile dogrulanacak sekilde surum `0.4.10` yapildi.

## v0.4.x Summary

- v0.4.0 Stable Core: Module, Event, Setting, Config/Profile, Carbon Menu ve HUD Editor stabilize edildi.
- v0.4.2 Fullbright Visual: Visuals sekmesine Fullbright eklendi.
- v0.4.3 Time Changer Visual: Client-side time visual ayari eklendi.
- v0.4.4 HUD polish: FPS/CPS polish ve Keystrokes Space eklendi.
- v0.4.5 Reach Display: Son hit mesafesi bilgi HUD'u eklendi.
- v0.4.6 Combo Display: Combo bilgi HUD'u eklendi.
- v0.4.7 PvP HUD QA: Reach + Combo event zinciri ve runtime state kontrol edildi.
- v0.4.8 Clock HUD: Local time bilgi HUD'u eklendi.
- v0.4.9 HUD Modules QA: HUD modules, Options ve Config/Profile akislari tekrar kontrol edildi.
- v0.4.10 Release Candidate QA: 1.8.9 final RC dokumantasyon ve build dogrulamasi yapildi.

## v0.4.9 - HUD Modules QA Pass

- HUD Editor, Options, Config/Profile/Reset ve Mods Tab akislari mevcut HUD modulleriyle tekrar kontrol edildi.
- Reach Display, Combo Display ve Clock HUD runtime state davranislari kalici config/profile snapshot'larindan ayri tutulur.
- Crosshair center-fixed kalir; HUD Editor'a dahil edilmez.
- Fullbright ve Time Changer Visuals sekmesinde kalir; Mods Tab ve HUD Editor'a dahil edilmez.
- Scoreboard, BlockOverlay, Chat, Weather, Zoom veya Capture sistemi eklenmedi.

## v0.4.8 - Clock HUD

- HUD kategorisine Clock HUD bilgi modulu eklendi.
- Local sistem saati 24H/12H, seconds ve prefix secenekleriyle gosterilebilir.
- Clock HUD Minecraft world time veya Time Changer ile baglantili degildir; sadece client-side local time render eder.
- Clock HUD, HUD Editor, Config, Profiles ve Reset sistemleriyle uyumludur.

## v0.4.7 - PvP HUD QA Pass

- Reach Display ve Combo Display ortak AttackEntityEvent zinciri uzerinden birlikte kontrol edildi.
- Reach Display eye-to-bounding-box mesafe hesabini korur ve world/player yokken runtime mesafeyi guvenli sifirlar.
- Combo Display pending hit, hurtTime confirmation ve debounce mantigini korur; world/player yokken runtime combo state guvenli temizlenir.
- Runtime PvP HUD degerleri config/profile snapshot'larina yazilmaz; sadece ayarlar ve HUD pozisyonlari kalici tutulur.
- Attack/damage eventleri cancel edilmez; packet, reach, hitbox veya combat davranisina mudahale yoktur.

## v0.4.6 - Combo Display

- PVP kategorisine Combo Display bilgi HUD'u eklendi.
- Basarili hitlerde combo sayisi artar, local player entity kaynakli hasar alinca sifirlanir.
- Reset Delay ayariyla yeni hit gelmezse combo client-side olarak sifirlanir.
- Attack veya damage eventleri cancel edilmez; packet, hitbox, reach veya combat davranisina mudahale yoktur.
- Combo Display HUD Editor, Config, Profiles ve Reset sistemleriyle uyumludur.

## v0.4.5 - Reach Display

- PVP kategorisine Reach Display bilgi HUD'u eklendi.
- Son basarili hit mesafesi client-side olarak gosterilir.
- Reach artırma, hitbox degistirme, packet gonderme veya combat davranisina mudahale yoktur.
- Reach Display HUD Editor, Config, Profiles ve Reset sistemleriyle uyumludur.

## v0.4.4 - HUD Polish

- FPS Display ve CPS Display icin Style Mode ayari eklendi.
- FPS/CPS background acik/kapali bounds ve padding davranisi daha tutarli hale getirildi.
- Keystrokes icin Style Mode, Show Background ve Show Space ayarlari eklendi.
- Keystrokes HUD'a genis SPACE satiri eklendi; basili durumda Pressed Color ile highlight olur.

## v0.4.3 - Visuals / Time Changer

- Visuals sekmesine Time Changer eklendi.
- Time Changer klasik ModuleManager modulu degildir; Mods Tab ve HUD Editor'da gorunmez.
- Day, Night, Sunset, Sunrise ve Custom client-side time modlari eklendi.
- Time Changer ayarlari config/profile snapshot'larina `visuals.timeChanger` olarak kaydedilir.

## v0.4.2 - Visuals / Fullbright

- Carbon Menu icine Visuals sekmesi eklendi.
- Fullbright klasik mod karti olmadan Visuals altinda yonetilir.
- Fullbright ayarlari config/profile snapshot'larina `visuals.fullbright` olarak kaydedilir.
- Runtime gamma restore akisi eklendi; kullanicinin orijinal brightness ayari kalici olarak ezilmez.

## v0.4.1 - Scoreboard Removal

- Scoreboard module tamamen kaldirildi.
- Vanilla sidebar render/cancel override ve ilgili event/hook denemeleri temizlendi.
- Eski Scoreboard config/profile verileri kayitli mod listesinde olmadigi icin yok sayilir ve sonraki save'de tekrar yazilmaz.
- Scoreboard customization daha sonra guvenli vanilla render hook arastirmasindan sonra ele alinacak.

## v0.4.0 - Stable Core Release

- 1.8.9 Forge tarafi icin ilk stable core release hazirligi yapildi.
- Module, Event, Setting, Config ve Profile sistemleri birlikte calisacak sekilde stabilize edildi.
- Carbon Menu; Mods Tab search, scroll ve category filters ile duzenlendi.
- Options screen, Keybinds panel, Color Picker ve HUD Layout Editor stable core kapsaminda dogrulandi.
- Notification / Toast sistemi merkezi kullanici geri bildirimi icin hazirlandi.
- FPS, CPS, Keystrokes, ToggleSprint, Armor HUD, Potion HUD, Coordinates HUD, Ping Display ve Crosshair Editor mevcut core moduller olarak dogrulandi.
- Test amacli gecici module aktif client'tan kaldirildi.
- Resource metadata eklendi ve release jar surumu `0.4.0` olarak ayarlandi.

## v0.3.x Ozeti

- Carbon UI Framework ve Carbon Design System temelleri eklendi.
- Mods Tab tek scrollable grid, search ve category filter altyapisina tasindi.
- Profiles system, Notification system ve Config defaults/reset sistemi gelistirildi.
- HUD modulleri, Crosshair Editor ve Options preview akislari genisletildi.
