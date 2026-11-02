# MiHealth Alias (LSPosed modülü)

HyperOS widget seçicisinde (com.miui.personalassistant) kurulu Mi Fitness'i (com.xiaomi.wearable)
Çin uygulaması (com.mi.health) gibi gösterir. Saat bağlantısına dokunmaz, sadece seçici işleminde çalışır.

## 1) GitHub'da derle
1. github.com'da yeni bir **özel (private)** depo aç (ör. `MiHealthAlias`).
2. Bu klasörün içindekileri depoya yükle (Add file > Upload files). `.github` klasörü gizli
   olduğu için görünmeyebilir. Yüklenmediyse: Add file > Create new file, ad olarak
   `.github/workflows/build.yml` yaz ve bu dosyanın içeriğini yapıştır.
3. Depoda **Actions** sekmesine gir, "Build module" çalışmasının bitmesini bekle (3-6 dk).
4. Çalışmaya tıkla, en altta **Artifacts** altından `MiHealthAlias-debug` dosyasını indir,
   zip'ten çıkan `.apk`'yı telefona at.
   Derleme kırmızı biterse hata satırını (kırmızı adım) bana yapıştır.

## 2) Telefonda
1. Önce yeniden paketlenmiş `com.mi.health`'i sil, orijinal `com.xiaomi.wearable` Mi Fitness'i kur.
2. `MiHealthAlias.apk`'yı kur, LSPosed > Modüller > MiHealth Alias'ı **etkinleştir**.
3. Kapsamda **Kişisel asistan / App Vault (com.miui.personalassistant)** seç
   (sistem uygulamalarını göster açık olmalı). Başka bir şey seçme.
4. Şunu çalıştır:
   `su -c "am force-stop com.miui.personalassistant; am force-stop com.miui.home"`
5. Ana ekrana uzun bas > Widget'lar, Mi Fitness / Mi Sağlık widget'larını ara.

## Kapatmak
LSPosed'dan modülü kapat ya da kapsamdan çıkar, 4. adımdaki komutu tekrarla.
Günlük: LSPosed > Günlükler, "MiHealthAlias:" satırlarına bak.
