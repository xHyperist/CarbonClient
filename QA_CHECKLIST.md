# Carbon Client Manual QA Checklist

## Client Launch

- [ ] `.\gradlew.bat runClient` ile client aciliyor.
- [ ] Loglarda `Carbon Client v0.5.1 is starting.` gorunuyor.
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
