# Carbon Client Manual QA Checklist

## Client Launch

- [ ] `.\gradlew.bat runClient` ile client aciliyor.
- [ ] Loglarda `Carbon Client v0.4.8 is starting.` gorunuyor.
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
