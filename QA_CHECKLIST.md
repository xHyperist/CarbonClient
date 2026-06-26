# Carbon Client Manual QA Checklist

## Client Launch

- [ ] `.\gradlew.bat runClient` ile client aciliyor.
- [ ] Loglarda `Carbon Client v0.6.6 is starting.` gorunuyor.
- [ ] Loglarda `Carbon Client initialized successfully.` gorunuyor.
- [ ] Carbon kaynakli crash yok.

## Carbon Menu

- [ ] `RSHIFT` Carbon Menu'yu aciyor.
- [ ] `RSHIFT` veya `ESC` menuyu kapatiyor.
- [ ] Oyun menu acikken pause olmuyor.

## Visuals

- [ ] Visuals sekmesi gorunuyor.
- [ ] Fullbright Mods Tab'da mod karti olarak gorunmuyor.
- [ ] Fullbright ON/OFF toggle calisiyor.
- [ ] Fullbright acilinca client-side brightness artiyor.
- [ ] Fullbright kapaninca eski gamma degeri geri geliyor.
- [ ] Brightness Level slider calisiyor.
- [ ] Smooth Transition toggle calisiyor.
- [ ] Config/Profile degisimlerinde Fullbright ayarlari korunuyor.
- [ ] Reset All Settings Fullbright'i default false durumuna donduruyor.
- [ ] Time Changer Visuals sekmesinde gorunuyor.
- [ ] Time Changer Mods Tab'da mod karti olarak gorunmuyor.
- [ ] Time Changer HUD Editor'da gorunmuyor.
- [ ] Time Changer ON/OFF toggle calisiyor.
- [ ] Day, Night, Sunset, Sunrise modlari client-side zamani degistiriyor.
- [ ] Custom mode icinde Custom Time slider calisiyor.
- [ ] Time Changer kapaninca vanilla/server time akisina geri donuyor.
- [ ] Config/Profile degisimlerinde Time Changer ayarlari korunuyor.
- [ ] Reset All Settings Time Changer'i default false durumuna donduruyor.

## Mods Tab

- [ ] ALL filtresi tum modlari gosteriyor.
- [ ] HUD filtresi HUD modullerini gosteriyor.
- [ ] RENDER filtresi Crosshair gibi render modullerini gosteriyor.
- [ ] MOVEMENT filtresi ToggleSprint'i gosteriyor.
- [ ] PVP ve UTILITY bossa `No mods found` duzgun gorunuyor.
- [ ] Search + category birlikte calisiyor.
- [ ] Search aktifken mod keybindleri tetiklenmiyor.
- [ ] Scroll footer/header/search/filter alanina tasmiyor.
- [ ] Enable/Disable ve Options dogru karta uygulaniyor.

## Options Screen

- [ ] Her modun Options ekrani aciliyor.
- [ ] Position X/Y Options icinde gorunmuyor.
- [ ] Toggle, Mode, Slider ve Color ayarlari degisiyor.
- [ ] Color Picker aciliyor ve ayarlari canli uyguluyor.
- [ ] Crosshair Preview ayarlarla birlikte guncelleniyor.
- [ ] Reset to Defaults sadece secili modu sifirliyor.

## Keybinds

- [ ] Keybind Change calisiyor.
- [ ] ESC keybind degistirmeyi iptal ediyor.
- [ ] DELETE/BACKSPACE keybind'i NONE yapiyor.
- [ ] Reset keybind default tusa donduruyor.
- [ ] Ayni tus iki modda olunca conflict warning gorunuyor.

## Profiles

- [ ] Default profil ilk acilista yukleniyor.
- [ ] Create Profile temiz/default profil olusturuyor.
- [ ] Duplicate Profile aktif profilin kopyasini olusturuyor.
- [ ] Save Current Profile sadece aktif profili kaydediyor.
- [ ] Load Profile mod, keybind, setting ve HUD konumlarini uyguluyor.
- [ ] Rename Profile calisiyor.
- [ ] Delete Profile iki tiklamali confirmation istiyor.
- [ ] Default profil silinemiyor.
- [ ] Bozuk/eksik profil dosyasi crash atmiyor.

## Notifications

- [ ] Mod ac/kapat notification gosteriyor.
- [ ] Reset notification gosteriyor.
- [ ] Keybind change/conflict notification gosteriyor.
- [ ] Profile notification gosteriyor.
- [ ] HUD Editor save notification gosteriyor.
- [ ] Bildirimler sag altta stack oluyor ve sure bitince kayboluyor.

## HUD Editor

- [ ] FPS, CPS, Keystrokes, ToggleSprint, Armor HUD, Potion HUD, Coordinates HUD ve Ping Display tasinabiliyor.
- [ ] Reach Display PVP kategorisinde gorunuyor ve HUD Editor'da tasinabiliyor.
- [ ] Reach Display default disabled geliyor.
- [ ] Reach Display acikken surekli `0.00 Blocks` veya son hit mesafesini gosteriyor.
- [ ] Reach Display entity hit sonrasi mesafeyi her zaman iki ondalikli `%BLOCK% Blocks` formatinda guncelliyor.
- [ ] Reach Display Options icinde sadece Show Background, Scale, Text Color ve Background Color gorunuyor.
- [ ] Reach Display eski config/profile alanlari crash atmadan yok sayiliyor ve sonraki save'de tekrar yazilmiyor.
- [ ] Combo Display PVP kategorisinde gorunuyor ve HUD Editor'da tasinabiliyor.
- [ ] Combo Display default disabled geliyor.
- [ ] Combo Display acikken `Combo: 0` gosteriyor.
- [ ] Entity hit sonrasi Combo Display sayaci artiyor.
- [ ] Reset Delay minimum 10 saniye ve 10 saniyenin altina indirilemiyor.
- [ ] Reset Delay suresi gecince Combo Display sayaci 0'a donuyor.
- [ ] Local player entity kaynakli hasar alinca Combo Display sayaci 0'a donuyor.
- [ ] Combo Display hit/damage eventlerini cancel etmiyor ve runtime combo count config/profile'a kaydedilmiyor.
- [ ] Clock HUD HUD kategorisinde gorunuyor ve HUD Editor'da tasinabiliyor.
- [ ] Clock HUD default disabled geliyor.
- [ ] Clock HUD local sistem saatini gosteriyor ve Time Changer ile karismiyor.
- [ ] Clock HUD Show Seconds, 24H/12H, Show Prefix ve Prefix Text ayarlari calisiyor.
- [ ] Clock HUD Options icinde Position X/Y gorunmuyor.
- [ ] FPS/CPS background acik ve kapali iken bounds/padding dogru.
- [ ] FPS/CPS Style Mode Modern, Classic ve Minimal calisiyor.
- [ ] Keystrokes icinde SPACE satiri gorunuyor.
- [ ] Space basili iken SPACE satiri Pressed Color ile highlight oluyor.
- [ ] Show Space false iken Keystrokes bounds kuculuyor.
- [ ] Keystrokes Style Mode Modern, Classic ve Minimal calisiyor.
- [ ] Crosshair HUD Editor'da gorunmuyor.
- [ ] Secili HUD ogesi vurgulaniyor.
- [ ] Ekran disina tasan HUD pozisyonlari guvenli sekilde clamp ediliyor.
- [ ] ESC ile cikinca HUD layout kaydediliyor.

## Config / Persistence

- [ ] Config silinirse defaultlar ile yeniden olusuyor.
- [ ] Bozuk config crash atmiyor.
- [ ] Eski/silinmis mod config verileri sonraki save'de temizleniyor.
- [ ] Reset All Settings tum sistemi defaulta donduruyor.
- [ ] Restart sonrasi ayarlar korunuyor.

## Game Context

- [ ] Singleplayer'da crash yok.
- [ ] Multiplayer'da crash yok.
- [ ] Ping alinamazsa `N/A` gosteriliyor.
- [ ] World/player null durumlarinda HUD render crash atmiyor.

## Later / Research Required

- [ ] Scoreboard customization guvenli vanilla render hook arastirmasindan sonra yeniden degerlendirilecek.

## v0.4.10 Final Release Candidate QA

- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.
- [ ] Jar `build/libs/carbon-client-0.4.10.jar` olarak uretiliyor.
- [ ] `RSHIFT` Carbon Menu'yu aciyor.
- [ ] Mods Tab search/category/scroll duplicate kart gostermiyor.
- [ ] Options ekraninda Position X/Y hicbir modda gorunmuyor.
- [ ] Keybinds panel ve conflict warning calisiyor.
- [ ] Profiles create/duplicate/load/save/delete akislari calisiyor.
- [ ] Visuals sekmesinde Fullbright ve Time Changer calisiyor.
- [ ] Fullbright ve Time Changer Mods Tab/HUD Editor'da gorunmuyor.
- [ ] Notification stack mod toggle, reset, profile ve HUD save akislariyla calisiyor.
- [ ] HUD Editor tum draggable HUD modullerini tasiyabiliyor.
- [ ] Crosshair HUD Editor'da gorunmuyor ve center-fixed kaliyor.
- [ ] Color Picker aciliyor, drag/scroll davranisi bozulmuyor.
- [ ] Eski Scoreboard/Zoom/Reach config alanlari crash atmadan yok sayiliyor.
- [ ] Save sonrasi kaldirilmis eski mod/ayar alanlari tekrar yazilmiyor.
- [ ] Reach/Combo/Clock runtime degerleri config/profile'a kalici yazilmiyor.
- [ ] Singleplayer acilista crash yok.
- [ ] Multiplayer baglantida crash yok.
- [ ] Restart sonrasi config/profile persistence korunuyor.
- [ ] PvP HUD modulleri attack/damage event cancel etmiyor.
- [ ] Packet/reach/hitbox/aim assist/autoclicker/aura/triggerbot davranisi yok.
- [ ] Scoreboard, BlockOverlay, Chat, Weather, Zoom ve Capture aktif mod olarak geri gelmedi.

## v0.5.0 Multi-Version Architecture Planning QA

- [ ] 1.8.9 `.\gradlew.bat clean build` hala BUILD SUCCESSFUL veriyor.
- [ ] Jar `build/libs/carbon-client-0.5.0.jar` olarak uretiliyor.
- [ ] 1.7.10 kodu henuz eklenmedi.
- [ ] `build.gradle` multi-project yapisina cevrilmedi.
- [ ] Mevcut source set yapisi degistirilmedi.
- [ ] Launcher/auth/cosmetic kodu eklenmedi.
- [ ] Scoreboard/Zoom/BlockOverlay/Chat/Weather/Capture aktif mod olarak geri gelmedi.
- [ ] `MULTI_VERSION_ARCHITECTURE_PLAN.md` mevcut.
- [ ] README multi-version plan dokumanina referans veriyor.
- [ ] Reference.java ve gradle.properties surumu `0.5.0`.

## v0.5.1 Safe Bridge Preparation QA

- [ ] Bridge packages `com.carbonclient.bridge` altinda mevcut.
- [ ] Bridge interfaces compile oluyor.
- [ ] `BridgeRegistry` compile oluyor.
- [ ] `BridgeVersionInfo` 1.8.9 icin `PLANNED` durumunu tasiyor.
- [ ] Mevcut 1.8.9 modulleri hala calisiyor.
- [ ] Henuz hicbir modul bridge sistemine tasinmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` ve source-set yapisi degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.1.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.2 Bridge Documentation + Dependency Mapping QA

- [ ] `BRIDGE_DEPENDENCY_MAP_v0.5.2.md` mevcut.
- [ ] Existing bridge interfaces hala compile oluyor.
- [ ] `BridgeRegistry` hala compile oluyor.
- [ ] Henuz hicbir modul bridge sistemine tasinmadi.
- [ ] Bridge implementasyonu yazilmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Source set / multi-project yapisina gecilmedi.
- [ ] Scoreboard/Zoom/BlockOverlay/Chat/Weather/Capture aktif mod olarak geri gelmedi.
- [ ] Jar `build/libs/carbon-client-0.5.2.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.3 Passive 1.8.9 Bridge Implementation QA

- [ ] `V189BridgeBootstrap.bootstrap()` guvenli sekilde calisiyor.
- [ ] `BridgeRegistry.hasGameBridge()` bootstrap sonrasi true.
- [ ] `BridgeRegistry.hasInputBridge()` bootstrap sonrasi true.
- [ ] `BridgeRegistry.hasRenderBridge()` bootstrap sonrasi true.
- [ ] World/Entity/Config bridge henuz register edilmediyse false kalabiliyor.
- [ ] Existing modules hala eski direct 1.8.9 path ile calisiyor.
- [ ] Henuz hicbir modul bridge sistemine tasinmadi.
- [ ] `RenderUtils`, `CarbonMenuScreen`, `HudLayoutEditorScreen`, `ForgeEventBridge` bridge'e tasinmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Source set / multi-project yapisina gecilmedi.
- [ ] Scoreboard/Zoom/BlockOverlay/Chat/Weather/Capture aktif mod olarak geri gelmedi.
- [ ] Jar `build/libs/carbon-client-0.5.3.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.4 Passive Bridge Runtime QA

- [ ] Bridge bootstrap idempotent.
- [ ] Bridge bootstrap hata durumunda client'i crash ettirmiyor.
- [ ] `BridgeRegistry` null register isteklerini yok sayiyor.
- [ ] `BridgeRegistry` get/has methodlari tutarli.
- [ ] `V189GameBridge` null world/player durumunda guvenli deger donduruyor.
- [ ] `V189InputBridge` uncreated Keyboard/Mouse durumunda guvenli deger donduruyor.
- [ ] `V189RenderBridge` null text/font durumunda crash atmiyor.
- [ ] `com.carbonclient.bridge.api.*` altinda Minecraft/Forge importu yok.
- [ ] `BridgeRegistry` Minecraft/Forge importu icermiyor.
- [ ] Mevcut moduller hala direct 1.8.9 path ile calisiyor.
- [ ] `RenderUtils`, `CarbonMenuScreen`, `HudLayoutEditorScreen`, `ForgeEventBridge`, `KeyInputHandler` bridge'e tasinmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.4.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.5 Bridge Diagnostics / Internal Validation QA

- [ ] `BridgeDiagnostics` compile oluyor.
- [ ] `BridgeDiagnosticsReport` compile oluyor.
- [ ] Diagnostics package Minecraft/Forge/LWJGL importu icermiyor.
- [ ] Core passive bridges available raporlanabiliyor.
- [ ] Event/Entity/World/Config bridge unavailable kalabilir ve bu hata degil.
- [ ] Diagnostics user-facing UI, HUD, chat command, keybind veya notification eklemiyor.
- [ ] Existing modules hala direct 1.8.9 path ile calisiyor.
- [ ] `RenderUtils`, `CarbonMenuScreen`, `HudLayoutEditorScreen`, `ForgeEventBridge`, `KeyInputHandler` bridge'e tasinmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.5.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.6 FPS Display Bridge-Assisted Render Prototype QA

- [ ] FPS Display bridge-assisted render path bridge available iken deneniyor.
- [ ] FPS Display bridge yoksa legacy render path'e guvenli dusuyor.
- [ ] Bridge render exception durumunda legacy fallback calisiyor.
- [ ] FPS Display settings degismedi.
- [ ] FPS Display config/profile icin yeni alan eklenmedi.
- [ ] FPS HUD Editor bounds degismedi.
- [ ] Show Background, Scale, Text Color, Background Color ve Style Mode calisiyor.
- [ ] CPS/Keystrokes/ToggleSprint/Armor/Potion/Coordinates/Ping/Crosshair/Reach/Combo/Clock bridge'e tasinmadi.
- [ ] `RenderUtils`, `CarbonMenuScreen`, `HudLayoutEditorScreen`, `ForgeEventBridge`, `KeyInputHandler` bridge'e tasinmadi.
- [ ] `bridge.api` ve `bridge.diagnostics` import boundary bozulmadi.
- [ ] 1.7.10 kodu veya dependency eklenmedi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.6.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.7 FPS Bridge Consumer QA

- [ ] FPS bridge path `BridgeDiagnostics.isPassiveBridgeReady()` kontrol ediyor.
- [ ] FPS bridge path `BridgeRegistry.hasRenderBridge()` kontrol ediyor.
- [ ] FPS bridge path null `RenderBridge` durumunda legacy fallback'e dusuyor.
- [ ] FPS bridge path bridge exception durumunda legacy fallback'e dusuyor.
- [ ] FPS bridge path invalid width/height durumunda legacy fallback'e dusuyor.
- [ ] Bridge path basarili olursa duplicate FPS render olmuyor.
- [ ] FPS options degismedi.
- [ ] FPS config/profile icin yeni alan eklenmedi.
- [ ] FPS HUD Editor bounds legacy hesapla kaldi.
- [ ] FPS disinda yeni bridge consumer eklenmedi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.7.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.8 CPS Display Bridge-Assisted Render Prototype QA

- [ ] CPS Display bridge-assisted render path bridge available iken deneniyor.
- [ ] CPS Display bridge yoksa legacy render path'e guvenli dusuyor.
- [ ] CPS Display bridge exception durumunda legacy fallback calisiyor.
- [ ] CPS Display invalid width/height durumunda legacy fallback calisiyor.
- [ ] CPS counting LMB-only kaldi.
- [ ] Right CPS / Total CPS eklenmedi.
- [ ] CPS settings degismedi.
- [ ] CPS config/profile icin yeni alan eklenmedi.
- [ ] CPS HUD Editor bounds degismedi.
- [ ] FPS Display hala calisiyor.
- [ ] CPS disinda yeni bridge consumer eklenmedi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.8.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.9 CPS Bridge Consumer QA

- [ ] CPS Display bridge-assisted render path kontrol edildi.
- [ ] CPS Display legacy fallback kontrol edildi.
- [ ] CPS counting LMB-only kaldi.
- [ ] Right CPS / Total CPS eklenmedi.
- [ ] CPS son 1000 ms sol tik penceresini kullanmaya devam ediyor.
- [ ] CPS settings degismedi.
- [ ] CPS config/profile icin yeni alan eklenmedi.
- [ ] CPS HUD Editor bounds degismedi.
- [ ] FPS ve CPS disinda yeni bridge consumer eklenmedi.
- [ ] Clock HUD bridge'e tasinmadi.
- [ ] Coordinates HUD bridge'e tasinmadi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.9.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.10 Clock HUD Bridge-Assisted Render Prototype QA

- [ ] Clock HUD bridge-assisted render path bridge available iken deneniyor.
- [ ] Clock HUD bridge yoksa legacy render path'e guvenli dusuyor.
- [ ] Clock HUD bridge exception durumunda legacy fallback calisiyor.
- [ ] Clock HUD invalid width/height durumunda legacy fallback calisiyor.
- [ ] Clock local/system-time mantigi degismedi.
- [ ] Clock 24H format mantigi degismedi.
- [ ] Clock 12H format mantigi degismedi.
- [ ] Show Seconds mantigi degismedi.
- [ ] Show Prefix ve Prefix Text mantigi degismedi.
- [ ] Clock settings degismedi.
- [ ] Clock config/profile icin yeni alan eklenmedi.
- [ ] Clock HUD Editor bounds degismedi.
- [ ] FPS Display hala calisiyor.
- [ ] CPS Display hala calisiyor.
- [ ] Clock disinda yeni bridge consumer eklenmedi.
- [ ] Coordinates HUD bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.10.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.11 Clock HUD Bridge Consumer QA

- [ ] Clock HUD bridge-assisted render path kontrol edildi.
- [ ] Clock HUD legacy fallback kontrol edildi.
- [ ] Clock local/system-time mantigi degismedi.
- [ ] Minecraft world time kullanilmiyor.
- [ ] Time Changer Visual ile karismiyor.
- [ ] Clock runtime saat degeri config/profile'a yazilmiyor.
- [ ] Clock 24H format mantigi degismedi.
- [ ] Clock 12H format mantigi degismedi.
- [ ] Show Seconds mantigi degismedi.
- [ ] Show Prefix ve Prefix Text mantigi degismedi.
- [ ] Clock settings degismedi.
- [ ] Clock config/profile icin yeni alan eklenmedi.
- [ ] Clock HUD Editor bounds degismedi.
- [ ] FPS, CPS ve Clock disinda yeni bridge consumer eklenmedi.
- [ ] Coordinates HUD bridge'e tasinmadi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] Armor/Potion/Ping/Reach/Combo bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] Jar `build/libs/carbon-client-0.5.11.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.12 Low-Risk Bridge Consumers QA Pass

- [ ] FPS/CPS/Clock bridge consumers birlikte kontrol edildi.
- [ ] FPS Display bridge fallback kontrol edildi.
- [ ] CPS Display bridge fallback kontrol edildi.
- [ ] Clock HUD bridge fallback kontrol edildi.
- [ ] FPS/CPS/Clock legacy fallback zorunlu kaldi.
- [ ] FPS/CPS/Clock config/profile formatlari degismedi.
- [ ] FPS/CPS/Clock HUD Editor bounds legacy hesapta kaldi.
- [ ] FPS runtime value config/profile'a yazilmiyor.
- [ ] CPS click list/value config/profile'a yazilmiyor.
- [ ] Clock runtime time config/profile'a yazilmiyor.
- [ ] FPS/CPS/Clock disinda yeni bridge consumer eklenmedi.
- [ ] Coordinates HUD bridge'e tasinmadi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] Diger HUD modulleri bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] HudLayoutEditorScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.12.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.13 Coordinates HUD Bridge-Assisted Render Prototype QA

- [ ] Coordinates HUD bridge-assisted render path bridge available iken deneniyor.
- [ ] Coordinates HUD bridge yoksa legacy render path'e guvenli dusuyor.
- [ ] Coordinates HUD bridge exception durumunda legacy fallback calisiyor.
- [ ] Coordinates HUD invalid width/height/font metric durumunda legacy fallback calisiyor.
- [ ] X/Y/Z hesaplama mantigi degismedi.
- [ ] Direction/yön mantigi degismedi.
- [ ] Biome okuma mantigi degismedi.
- [ ] Player/world data access legacy/direct 1.8.9 path'te kaldi.
- [ ] EntityBridge/WorldBridge kullanilmadi.
- [ ] Coordinates settings degismedi.
- [ ] Coordinates config/profile icin yeni alan eklenmedi.
- [ ] Coordinates HUD Editor bounds degismedi.
- [ ] FPS/CPS/Clock hala calisiyor.
- [ ] Coordinates disinda yeni bridge consumer eklenmedi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] Diger HUD modulleri bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.13.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.14 Coordinates HUD Bridge Consumer QA

- [ ] Coordinates HUD bridge-assisted render path kontrol edildi.
- [ ] Coordinates HUD legacy fallback kontrol edildi.
- [ ] X/Y/Z hesaplama mantigi degismedi.
- [ ] Direction/yon mantigi degismedi.
- [ ] Biome okuma mantigi degismedi.
- [ ] Player/world null durumlari guvenli kaldi.
- [ ] Coordinates runtime value config/profile'a yazilmiyor.
- [ ] Coordinates options degismedi.
- [ ] Coordinates config/profile icin yeni alan eklenmedi.
- [ ] Coordinates HUD Editor bounds degismedi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] GameBridge veri okuma icin kullanilmadi.
- [ ] FPS/CPS/Clock hala calisiyor.
- [ ] Coordinates disinda yeni bridge consumer eklenmedi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] Diger HUD modulleri bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.14.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.15 Low-Risk Render Bridge Consumers QA Pass II

- [ ] FPS/CPS/Clock/Coordinates bridge consumers birlikte kontrol edildi.
- [ ] FPS Display legacy fallback kontrol edildi.
- [ ] CPS Display legacy fallback kontrol edildi.
- [ ] Clock HUD legacy fallback kontrol edildi.
- [ ] Coordinates HUD legacy fallback kontrol edildi.
- [ ] FPS/CPS/Clock/Coordinates config/profile formatlari degismedi.
- [ ] FPS/CPS/Clock/Coordinates HUD Editor bounds legacy hesapta kaldi.
- [ ] Coordinates data access direct 1.8.9 path'te kaldi.
- [ ] Coordinates render-only bridge-assisted kaldi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] GameBridge veri okuma icin kullanilmadi.
- [ ] Keystrokes bridge'e tasinmadi.
- [ ] Armor/Potion/Ping/Crosshair/Reach/Combo bridge'e tasinmadi.
- [ ] Fullbright/Time Changer bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] HudLayoutEditorScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] ConfigManager/ProfileManager bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.15.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.16 Keystrokes Partial Render Bridge Prototype QA

- [ ] Keystrokes bridge-assisted render path kontrol edildi.
- [ ] Keystrokes bridge hazir degilse legacy render path'e guvenli dusuyor.
- [ ] Keystrokes bridge exception durumunda legacy fallback calisiyor.
- [ ] Keystrokes invalid width/height/font metric durumunda legacy fallback calisiyor.
- [ ] W/A/S/D pressed logic degismedi.
- [ ] LMB/RMB pressed logic degismedi.
- [ ] SPACE pressed logic degismedi.
- [ ] InputBridge kullanilmadi.
- [ ] Keystrokes settings degismedi.
- [ ] Keystrokes config/profile icin yeni alan eklenmedi.
- [ ] Keystrokes HUD Editor bounds degismedi.
- [ ] Show Space acik/kapali bounds mevcut legacy mantikta kaldi.
- [ ] FPS/CPS/Clock/Coordinates hala calisiyor.
- [ ] Keystrokes disinda yeni bridge consumer eklenmedi.
- [ ] Diger HUD modulleri bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] HudLayoutEditorScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.16.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.17 Keystrokes Bridge Consumer QA

- [ ] Keystrokes bridge-assisted render path kontrol edildi.
- [ ] Keystrokes legacy fallback kontrol edildi.
- [ ] Keystrokes bridge metric exception durumunda legacy fallback calisiyor.
- [ ] Keystrokes bridge draw exception durumunda legacy fallback calisiyor.
- [ ] W/A/S/D pressed logic degismedi.
- [ ] LMB/RMB pressed logic degismedi.
- [ ] SPACE pressed logic degismedi.
- [ ] Show Space false iken SPACE satiri gizli kaliyor.
- [ ] Show Space true iken SPACE satiri gorunuyor.
- [ ] Pressed Color mantigi degismedi.
- [ ] InputBridge kullanilmadi.
- [ ] KeyInputHandler degistirilmedi.
- [ ] Keystrokes options degismedi.
- [ ] Keystrokes config/profile icin yeni alan eklenmedi.
- [ ] Keystrokes HUD Editor bounds legacy hesapta kaldi.
- [ ] Position X/Y Options icinde gorunmuyor.
- [ ] FPS/CPS/Clock/Coordinates hala calisiyor.
- [ ] Keystrokes disinda yeni bridge consumer eklenmedi.
- [ ] Armor/Potion/Ping/Crosshair/Reach/Combo bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] HudLayoutEditorScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.17.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.18 Render Bridge Consumers QA Pass III

- [ ] FPS/CPS/Clock/Coordinates/Keystrokes bridge consumers birlikte kontrol edildi.
- [ ] FPS Display legacy fallback kontrol edildi.
- [ ] CPS Display legacy fallback kontrol edildi.
- [ ] Clock HUD legacy fallback kontrol edildi.
- [ ] Coordinates HUD legacy fallback kontrol edildi.
- [ ] Keystrokes legacy fallback kontrol edildi.
- [ ] Metric cagri kontrolleri renderWithBridge try/catch icinde kaliyor.
- [ ] FPS/CPS/Clock/Coordinates/Keystrokes config/profile formatlari degismedi.
- [ ] FPS/CPS/Clock/Coordinates/Keystrokes HUD Editor bounds legacy hesapta kaldi.
- [ ] Coordinates data access direct 1.8.9 path'te kaldi.
- [ ] Keystrokes key/mouse pressed state direct 1.8.9 path'te kaldi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] InputBridge kullanilmadi.
- [ ] Armor/Potion/Ping/ToggleSprint/Crosshair/Reach/Combo bridge'e tasinmadi.
- [ ] Fullbright/Time Changer bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] HudLayoutEditorScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] ConfigManager/ProfileManager bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.18.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.19 Render Bridge Helper Cleanup / Extraction QA

- [ ] RenderBridge helper eklendi.
- [ ] Helper safe RenderBridge readiness kontrolu yapiyor.
- [ ] Helper safe font height ve string width metric okuma sagliyor.
- [ ] Helper Minecraft/Forge/LWJGL import etmiyor.
- [ ] FPS Display helper kullanirken davranisi degismedi.
- [ ] CPS Display helper kullanirken LMB-only davranisi degismedi.
- [ ] Clock HUD helper kullanirken local/system time davranisi degismedi.
- [ ] Coordinates HUD helper kullanirken direct 1.8.9 data access degismedi.
- [ ] Keystrokes helper kullanirken direct key/mouse pressed state degismedi.
- [ ] Legacy fallback bes modulde de korunuyor.
- [ ] Config/profile formatlari degismedi.
- [ ] HUD Editor bounds legacy hesapta kaldi.
- [ ] Yeni bridge consumer eklenmedi.
- [ ] InputBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] Armor/Potion/Ping bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry`, `bridge.render` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.19.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.20 Render Bridge Helper QA

- [ ] RenderBridgeAccess helper kontrol edildi.
- [ ] Helper getIfReady null/fallback durumlarinda guvenli.
- [ ] Helper safeFontHeight null/exception/invalid metric durumlarinda guvenli.
- [ ] Helper safeStringWidth null text/exception/invalid metric durumlarinda guvenli.
- [ ] Helper module-specific logic icermiyor.
- [ ] Helper log/notification/UI uretmiyor.
- [ ] FPS/CPS/Clock/Coordinates/Keystrokes helper kullanimlari kontrol edildi.
- [ ] Legacy fallback bes modulde de korunuyor.
- [ ] Config/profile formatlari degismedi.
- [ ] HUD Editor bounds legacy hesapta kaldi.
- [ ] Yeni bridge consumer eklenmedi.
- [ ] InputBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] Ping/Armor/Potion bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry`, `bridge.render` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.20.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.21 Ping Display Render Bridge Prototype

- [ ] Ping Display bridge-assisted path ile render edebiliyor.
- [ ] Ping Display legacy render fallback'e guvenli donebiliyor.
- [ ] RenderBridgeAccess getIfReady/safeFontHeight/safeStringWidth kullaniliyor.
- [ ] Ping data/network/player info logic degismedi.
- [ ] Ping settings degismedi.
- [ ] Ping HUD Editor bounds legacy hesapta kaldi.
- [ ] Yeni config/profile alani eklenmedi.
- [ ] GameBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] InputBridge kullanilmadi.
- [ ] Armor/Potion bridge'e tasinmadi.
- [ ] Diger moduller bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry`, `bridge.render` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.21.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.22 Ping Display Bridge Consumer QA

- [ ] Ping Display bridge path kontrol edildi.
- [ ] Ping Display legacy fallback kontrol edildi.
- [ ] RenderBridgeAccess getIfReady/safeFontHeight/safeStringWidth kullanimi kontrol edildi.
- [ ] Ping data/network/player info logic degismedi.
- [ ] Singleplayer/server yok/null durumlari crash riski acisindan kontrol edildi.
- [ ] Ping options degismedi.
- [ ] Ping HUD Editor bounds legacy hesapta kaldi.
- [ ] Yeni config/profile alani eklenmedi.
- [ ] GameBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] InputBridge kullanilmadi.
- [ ] Yeni NetworkBridge olusturulmadi.
- [ ] Yeni bridge consumer eklenmedi.
- [ ] Armor/Potion bridge'e tasinmadi.
- [ ] Diger moduller bridge'e tasinmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `bridge.api`, `bridge.diagnostics`, `bridge.registry`, `bridge.render` import boundary bozulmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.22.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.5.23 Module Enabled State Persistence + Armor/Potion Risk Analysis

- [ ] Module enabled state restart sonrasi korunuyor.
- [ ] Module disabled state restart sonrasi korunuyor.
- [ ] Saved enabled state constructor default degerlerini load sonrasi eziyor.
- [ ] Config snapshot module enabled state yaziyor.
- [ ] Profile snapshot module enabled state yaziyor.
- [ ] Carbon Menu module card toggle config ve active profile save tetikliyor.
- [ ] Options/setting/keybind/color degisiklikleri config ve active profile save tetikliyor.
- [ ] Keybind ile module toggle config ve active profile save tetikliyor.
- [ ] Reset to Defaults default enabled state'e donuyor ve kaydediliyor.
- [ ] Reset All Settings default enabled state'e donuyor ve kaydediliyor.
- [ ] Shutdown save config ve active profile snapshot yaziyor.
- [ ] Profile load saved enabled state uyguluyor.
- [ ] v0.5.23-hotfix gercek git diff uzerinden denetlendi.
- [ ] Startup stale active profile reconciliation kontrol edildi.
- [ ] Eski/stale active profile snapshot loaded config'teki daha yeni enabled state'i ezmiyor.
- [ ] Active profile silent save notification/log spam uretmiyor.
- [ ] Config/profile compatibility kontrol edildi.
- [ ] Runtime-only values config/profile'a yazilmiyor.
- [ ] Armor HUD bridge migration yapilmadi.
- [ ] Potion HUD bridge migration yapilmadi.
- [ ] Armor HUD bridge risk analysis tamamlandi.
- [ ] Potion HUD bridge risk analysis tamamlandi.
- [ ] Yeni bridge consumer eklenmedi.
- [ ] InputBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] RenderUtils bridge'e tasinmadi.
- [ ] CarbonMenuScreen bridge'e tasinmadi.
- [ ] ForgeEventBridge bridge'e tasinmadi.
- [ ] KeyInputHandler bridge'e tasinmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.23.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.
- [ ] `.\gradlew.bat runClient` manuel persistence testi tamamlandi veya gerekcesi raporlandi.

## v0.5.24 Multi-Version Bridge Phase Review

- [ ] Bridge phase review completed.
- [ ] Render-only consumers documented.
- [ ] Legacy modules documented.
- [ ] Risk classification completed.
- [ ] v0.6.0 entry criteria documented.
- [ ] v0.6.x roadmap documented.
- [ ] `MULTI_VERSION_PHASE_REVIEW_v0.5.24.md` exists.
- [ ] No new bridge consumers added.
- [ ] No runtime behavior changes.
- [ ] InputBridge kullanilmadi.
- [ ] EntityBridge kullanilmadi.
- [ ] WorldBridge kullanilmadi.
- [ ] `build.gradle` degistirilmedi.
- [ ] 1.7.10 code/dependency eklenmedi.
- [ ] Jar `build/libs/carbon-client-0.5.24.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.0 Multi-Version Runtime Preparation

- [ ] v0.6.0 planning document added.
- [ ] Performance-first rule documented.
- [ ] PvP responsiveness rule documented.
- [ ] Rod responsiveness goals documented with legit boundaries.
- [ ] Common/version-specific strategy documented.
- [ ] Build strategy options documented.
- [ ] v0.6.x roadmap documented.
- [ ] v0.8 performance roadmap note documented.
- [ ] No new bridge consumers added.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No InputBridge/EntityBridge/WorldBridge usage added.
- [ ] No cheat/PvP advantage feature added.
- [ ] Jar `build/libs/carbon-client-0.6.0.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.1 1.7.10 Environment Strategy

- [ ] v0.6.1 strategy document added.
- [ ] Build strategy options documented.
- [ ] Recommended strategy documented.
- [ ] Performance-first requirement documented.
- [ ] PvP/rod responsiveness requirement documented.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No new branch created automatically.
- [ ] No runtime behavior changes.
- [ ] No new bridge consumers added.
- [ ] Jar `build/libs/carbon-client-0.6.1.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.2 Common vs Version-Specific Separation Plan

- [ ] v0.6.2 separation plan added.
- [ ] Common candidate packages documented.
- [ ] Version-specific candidate packages documented.
- [ ] Mixed/adapter-needed packages documented.
- [ ] Module separation table completed.
- [ ] Performance-first rule documented.
- [ ] PvP/rod responsiveness rule documented.
- [ ] No package move performed.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No new bridge consumers added.
- [ ] No runtime behavior changes.
- [ ] Jar `build/libs/carbon-client-0.6.2.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.3 Isolated 1.7.10 Branch Preparation

- [ ] v0.6.3 branch preparation document added.
- [ ] Manual branch creation steps documented.
- [ ] Safety rules documented.
- [ ] First experiment scope documented.
- [ ] Rollback plan documented.
- [ ] Performance-first branch rule documented.
- [ ] PvP/rod responsiveness branch rule documented.
- [ ] No branch created.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No runtime behavior changes.
- [ ] No new bridge consumers added.
- [ ] Jar `build/libs/carbon-client-0.6.3.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.4 Low-Risk 1.7.10 Port Candidate Review

- [ ] v0.6.4 candidate review document added.
- [ ] Low/medium/high risk candidates documented.
- [ ] First experiment order documented.
- [ ] Performance-first candidate rule documented.
- [ ] PvP/rod responsiveness candidate rule documented.
- [ ] No branch created.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No package move performed.
- [ ] No new bridge consumers added.
- [ ] No runtime behavior changes.
- [ ] Jar `build/libs/carbon-client-0.6.4.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.5 First Isolated 1.7.10 Runtime Experiment Readiness

- [ ] v0.6.5 readiness checklist document added.
- [ ] Stable 1.8.9 main branch protection documented.
- [ ] Manual branch name `experimental/1.7.10-runtime` documented.
- [ ] First experiment scope limited to minimal Forge 1.7.10 bootstrap.
- [ ] No HUD/module port in first experiment documented.
- [ ] Branch-only build rules documented.
- [ ] Success criteria documented.
- [ ] Rollback plan documented.
- [ ] ForgeGradle/JDK/mappings/lifecycle/render/input risks documented.
- [ ] Performance-first rule documented.
- [ ] PvP/rod responsiveness rule documented.
- [ ] No branch created.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No runtime code changes.
- [ ] Jar `build/libs/carbon-client-0.6.5.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.

## v0.6.6 Final 1.8.9 Pre-Branch Stability Checkpoint

- [ ] v0.6.6 stability checkpoint document added.
- [ ] Clean build checklist documented.
- [ ] runClient manual QA checklist documented.
- [ ] Config/profile persistence manual QA checklist documented.
- [ ] HUD modules manual QA checklist documented.
- [ ] Visual modules manual QA checklist documented.
- [ ] Carbon Menu / HUD Editor manual QA checklist documented.
- [ ] PvP HUD / stability manual QA checklist documented.
- [ ] Known deferred systems documented.
- [ ] Branch pre-open checklist documented.
- [ ] No branch created.
- [ ] No `build.gradle` changes.
- [ ] No 1.7.10 dependency added.
- [ ] No runtime behavior changes.
- [ ] Jar `build/libs/carbon-client-0.6.6.jar` olarak uretiliyor.
- [ ] `.\gradlew.bat clean build` BUILD SUCCESSFUL veriyor.
