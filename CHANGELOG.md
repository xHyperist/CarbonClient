# Changelog

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
