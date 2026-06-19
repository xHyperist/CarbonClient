# Carbon Client v0.4.0 Release Notes

## Amac

v0.4.0, Carbon Client'in Minecraft 1.8.9 Forge tarafindaki ilk stable core release hazirligidir. Bu surum yeni buyuk ozellik eklemek yerine mevcut core sistemleri daha temiz, test edilebilir ve release alinabilir hale getirir.

## One Cikan Sistemler

- Module, Event ve Setting altyapisi
- Config save/load, defaults ve reset sistemi
- Profiles system
- Notification / Toast system
- Carbon Menu
- Mods Tab search, scroll ve category filters
- Options screen ve Keybinds panel
- Profesyonel Color Picker
- HUD Layout Editor
- FPS, CPS, Keystrokes, ToggleSprint, Armor HUD, Potion HUD, Coordinates HUD, Ping Display
- Crosshair Editor ve Options preview

## Bilinen Notlar

- Bu surum yalnizca Minecraft 1.8.9 Forge icindir.
- 1.7.10 portu bu surumde yoktur; sonraki roadmap asamasinda planlanir.
- Carbon Launcher, version selector, account, cloud config, cosmetic ve cape sistemleri bu surumde implement edilmemistir.
- ForgeGradle 2.x ve eski Forge gelistirme ortami bazi bilinen warning loglari uretebilir.

## Manuel Test Onerileri

- `.\gradlew.bat runClient` ile client launch.
- `RSHIFT` ile Carbon Menu ac/kapat.
- Mods Tab search/filter/scroll testi.
- Her mod icin Options ve Reset to Defaults testi.
- Color Picker ve Crosshair Preview testi.
- Keybind change, NONE ve conflict warning testi.
- Profile create/duplicate/save/load/rename/delete testi.
- HUD Editor ile tum HUD ogelerini tasima testi.
- Config ve profile persistence icin client restart testi.
- Singleplayer ve mumkunse multiplayer acilis testi.
