# Carbon Client v0.1

Carbon Client, Minecraft 1.8.9 ve Forge 11.15.1.2318 tabanli bir PvP client
projesidir. Bu ilk asama yalnizca calisan, bos ve genisletilebilir mod
iskeletini icerir.

## Bu Asamada Hazir Olanlar

- Minecraft 1.8.9 Forge proje yapisi
- Carbon Client Forge giris noktasi
- `preInit`, `init` ve `postInit` yasam dongusu
- Java 8 ve Gradle wrapper ayarlari
- Mod bilgilerini gosteren `mcmod.info`
- Gelecekteki sistemler icin ayrilmis temiz paket plani

HUD modulleri, Click GUI ve config sistemi henuz eklenmedi. Bunlar sonraki
adimlarda tek tek eklenecek.

## Gereksinimler

1. 64-bit Java Development Kit 8 (JDK 8)
2. IntelliJ IDEA Community Edition veya Eclipse
3. Internet baglantisi (ilk Gradle kurulumunda kutuphaneler indirilir)

Java 9 veya daha yeni bir JDK, bu eski ForgeGradle surumuyle guvenilir
degildir. Proje icin JDK 8 secilmelidir.

Bu proje hazirlanirken bilgisayarda su JDK 8 kurulumu bulundu:

```text
C:\Program Files\Java\jdk1.8.0_181
```

Mevcut PowerShell oturumunda Gradle'a bu JDK'yi kullandirmak icin:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_181"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

Kurulumu kontrol etmek icin PowerShell'de:

```powershell
java -version
javac -version
```

Her iki komut da `1.8` ile baslayan bir surum gostermelidir. `javac`
bulunamiyorsa yalnizca JRE kuruludur; JDK 8 kurulmalidir.

## Ilk Kurulum

Proje klasorunde PowerShell acip sirayla calistir:

```powershell
.\gradlew.bat setupDecompWorkspace
.\gradlew.bat idea
```

Eclipse kullaniyorsan ikinci komut yerine:

```powershell
.\gradlew.bat eclipse
```

Ardindan IntelliJ IDEA'da klasoru ac ve Gradle projesi olarak ice aktar.
Project SDK ve Gradle JVM alanlarinda JDK 8 secili olmali.

## Oyunu Gelistirme Ortaminda Calistirma

IntelliJ Gradle penceresinden:

```text
Tasks > forgegradle > runClient
```

veya PowerShell'den:

```powershell
.\gradlew.bat runClient
```

Minecraft ana menusundeki `Mods` ekraninda `Carbon Client` gorunuyorsa
iskelet dogru calisiyor demektir.

## Carbon Client Su An Nasil Test Edilir?

Su an Carbon Client icin ayri bir launcher bulunmuyor. Proje bu asamada
Minecraft 1.8.9 uzerinde calisan bir Forge mod/client olarak test edilir.

Once projeyi derle:

```powershell
.\gradlew.bat build
```

Ardindan `build/libs/carbon-client-0.1.0.jar` dosyasini Minecraft
kurulumundaki `mods` klasorune at. Minecraft'i Forge 1.8.9 profiliyle
baslat ve ana menudeki `Mods` ekraninda `Carbon Client` modunun yuklendigini
kontrol et.

Proje surumu degistirildiginde JAR dosyasinin adindaki `0.1.0` bolumu de
yeni surum numarasiyla degisir.

## JAR Olusturma

```powershell
.\gradlew.bat clean build
```

Olusan mod dosyasi:

```text
build/libs/carbon-client-0.1.0.jar
```

## Projeyi Nasil Takip Ederim?

Projeyi takip ederken ana dosyalarin gorevleri kisaca soyledir:

- `CarbonClient.java`: Forge tarafindan yuklenen ana mod giris noktasi.
- `Client.java`: ModuleManager ve diger client sistemlerini baslatir.
- `Reference.java`: Mod kimligi, adi ve surum bilgilerini tutar.
- `Module.java`: Tum modullerin kullandigi temel acma ve kapatma yapisi.
- `ModuleCategory.java`: Modulleri HUD, render ve movement gibi gruplara ayirir.
- `ModuleManager.java`: Modulleri kaydeder, bulur ve acma-kapatma islemlerini yonetir.
- `FPSDisplayModule.java`: Oyunda guncel FPS degerini ekrana cizer.
- `build.gradle`: ForgeGradle ve derleme ayarlarini tanimlar.
- `gradle.properties`: Minecraft, Forge ve Carbon Client surumlerini tutar.
- `mcmod.info`: Minecraft'in `Mods` ekraninda gosterdigi mod bilgilerini tanimlar.

Yeni ozellikler eklendikce once ilgili modul sinifini, sonra
`ModuleManager` kaydini ve son olarak derleme sonucunu kontrol edebilirsin.

Ileride `Carbon Launcher`, Carbon Client'i kurmak ve baslatmak icin ayri bir
masaustu uygulamasi olarak eklenecek. Launcher henuz bu projenin mevcut
surumunun bir parcasi degildir.

## Mevcut Klasor Yapisi

```text
CarbonClient/
|-- build.gradle
|-- gradle.properties
|-- settings.gradle
|-- gradlew
|-- gradlew.bat
|-- gradle/wrapper/
`-- src/main/
    |-- java/com/carbonclient/
    |   |-- CarbonClient.java
    |   |-- client/Client.java
    |   `-- common/Reference.java
    `-- resources/
        |-- mcmod.info
        `-- assets/carbonclient/lang/
```

Planlanan paketler, ihtiyacimiz oldukca olusturulacak:

```text
com.carbonclient.module
com.carbonclient.event
com.carbonclient.render
com.carbonclient.config
com.carbonclient.gui
```

Bos klasorler kaynak kontrolunde tutulmadigi icin bu paketler simdilik
fiziksel olarak eklenmedi.

## Dosyalar Ne Yapiyor?

- `build.gradle`: ForgeGradle, Minecraft, mapping ve JAR derleme ayarlari.
- `gradle.properties`: Proje ve Minecraft surumlerinin tek merkezde tutuldugu yer.
- `CarbonClient.java`: Forge'un yukledigi ana mod sinifi.
- `Client.java`: Client tarafindaki sistemlerin gelecekte baslatilacagi merkez.
- `Reference.java`: Mod kimligi, adi ve surumu gibi sabit degerler.
- `mcmod.info`: Minecraft Mods ekraninda gorunen bilgiler.

## Sik Karsilasilan Sorunlar

### `JAVA_HOME is not set`

JDK 8 klasorunu `JAVA_HOME` olarak ayarla. Ornek:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_XXX"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

`XXX` kismini bilgisayarindaki gercek JDK surumuyle degistir.

### `Could not resolve ForgeGradle`

Internet baglantisini kontrol et ve tekrar dene:

```powershell
.\gradlew.bat --refresh-dependencies setupDecompWorkspace
```

### Yanlis Java surumu kullaniliyor

```powershell
.\gradlew.bat --version
```

JVM satiri Java 8 gostermelidir. IntelliJ icinde Gradle JVM ayarini da JDK 8
olarak sec.

### Gradle bellek hatasi

`gradle.properties` icindeki degeri gecici olarak dusur:

```properties
org.gradle.jvmargs=-Xmx1G
```
