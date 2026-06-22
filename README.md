# Carbon Client

Carbon Client, Minecraft 1.8.9 Forge tabanli, PvP odakli ve moduler bir client projesidir. v0.4.x serisi stable core release hattidir: module, setting, config, profile, notification, menu ve HUD altyapilari 1.8.9 tarafinda birlikte calisacak sekilde toparlanmistir.

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
build/libs/carbon-client-0.4.4.jar
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
- Crosshair Editor ve preview

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
5. Jar ile test etmek icin `build/libs/carbon-client-0.4.4.jar` dosyasini Minecraft 1.8.9 Forge `mods` klasorune koy.

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
- v0.5.x: 1.7.10 port architecture hazirligi
- v0.6.x: 1.7.10 first boot ve feature parity
- Later / research required: Scoreboard customization after safe vanilla render hook research
- Sonraki asama: Carbon Launcher ve version selector
