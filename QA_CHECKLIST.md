# Carbon Client Manual QA Checklist

## Client Launch

- [ ] `.\gradlew.bat runClient` ile client aciliyor.
- [ ] Loglarda `Carbon Client v0.4.0 is starting.` gorunuyor.
- [ ] Loglarda `Carbon Client initialized successfully.` gorunuyor.
- [ ] Carbon kaynakli crash yok.

## Carbon Menu

- [ ] `RSHIFT` Carbon Menu'yu aciyor.
- [ ] `RSHIFT` veya `ESC` menuyu kapatiyor.
- [ ] Oyun menu acikken pause olmuyor.

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
