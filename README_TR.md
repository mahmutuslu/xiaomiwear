# MiHealth Alias v2.1.0 — libxposed API 102

Mi Fitness (`com.xiaomi.wearable`) uygulamasını **yalnızca HyperOS App Vault widget seçicisine**
(`com.miui.personalassistant`) Mi Health (`com.mi.health`) takma adıyla gösteren
LSPosed modülü. Mi Fitness APK'sını, eşleşmiş saati veya sistem paketlerini değiştirmez.

## Sürüm 2.1.0: nihai paket ve Xiaomi Wear / Mi Fitness ikonu

- Turuncu halka şeklindeki Xiaomi Wear / Mi Fitness simgesi, modülün Android ve LSPosed girişinde görünür.
- Adaptif ve klasik ikon kaynakları eklendi.
- APK, GitHub Releases altından tek dosya olarak indirilir.
- Paket kimliği `com.example.mihealthalias` olarak korunmuştur; orijinal Mi Fitness paketini değiştirmez.

## API 102 geçişi

- Eski `de.robv.android.xposed` / `assets/xposed_init` tamamen kaldırıldı.
- Modern `io.github.libxposed:api:102.0.0` kullanılır (`compileOnly`).
- Giriş sınıfı `XposedModule`, hook'lar `Hooker` / `Chain.proceed(...)` kullanır.
- Giriş, kapsam ve API bildirimi `META-INF/xposed/` altındadır.
- Sadece `com.miui.personalassistant` işlemi hedeflenir; kapsam sabittir.
- Değişmeyen paket argümanları doğrudan geçirilir; yalnızca eşleşen adlar yeniden yazılır.
- Hook'lar kimlikli kayıtlıdır (API 102); otomatik hot reload **kapalıdır**.
- JVM birim testleri ve Actions APK metadata denetimi eklenmiştir.

**Gereksinim:** Modern libxposed API 102'yi uygulayan LSPosed sürümü.
Eski yalnızca XposedBridge API 93 sağlayan framework'ler bu sürümü yükleyemez.
Android minimum sürüm 8 (API 26) olarak korunur; gerçek davranış ROM ve
App Vault sürümüne göre değişebilir.

## Derleme ve APK

**Önerilen indirme:** [GitHub Releases — v2.1.0](https://github.com/mahmutuslu/xiaomiwear/releases/tag/v2.1.0)
altında bulunan `MiHealthAlias-v2.1.0-API102.apk` dosyasını yükle.
Alternatif: GitHub Actions → Build module → son başarılı çalışma → Artifacts →
`MiHealthAlias-API102-2.1.0` ZIP'i içindeki `app-debug.apk`.
Dağıtılan APK debug imzası taşır. Farklı CI derlemeleri farklı imzalar kullanabilir;
yalnızca aynı imzayla üretilmiş APK'lar birbirinin üzerine kurulabilir.
Kararlı güncelleme dağıtımı için kullanıcıya ait güvenli release imzası gereklidir. Kaynak koddan derleme:

```bash
gradle :app:testDebugUnitTest :app:assembleDebug --no-daemon
```

## Kurulum / kullanım

1. Resmî Mi Fitness (`com.xiaomi.wearable`) kurulu olsun. Sahte `com.mi.health` APK'sı yükleme.
2. API 102 uyumlu LSPosed'i doğrula. Önceki APK farklı debug anahtarıyla imzalandıysa
   `INSTALL_FAILED_UPDATE_INCOMPATIBLE` hatası alınır; eski MiHealth Alias APK'sını
   kaldırıp yenisini kur, ardından LSPosed modülünü tekrar etkinleştir.
3. Modül APK'sını kur, LSPosed'den etkinleştir. Kapsam **Kişisel Asistan / App Vault**
   (`com.miui.personalassistant`) olmalıdır.
4. App Vault'u kapatıp yeniden aç (gerekirse `su -c 'am force-stop com.miui.personalassistant'`).
5. Ana ekranın widget seçiminde Mi Fitness / Mi Health girişini kontrol et.

Loglar: LSPosed loglarında `MiHealthAlias` ve `API 102: ... hooks installed`
mesajlarını ara.

## Sınırlar ve geri alma

Bu modül yalnızca uygulama listeleri ve paket adı aramalarını eşler; sunucu tarafında
olmayan widget'ları oluşturamaz ve saat bağlantısını değiştirmez. APK'nin CI'da
derlenmesi **gerçek cihaz/ROM üzerinde widget'ın göründüğünü kanıtlamaz**.

Geri almak için LSPosed'de modülü devre dışı bırak ve App Vault'u yeniden başlat.
Önceki sürüme dönmek için gerekiyorsa bu sürümü kaldırıp eski APK'yı tekrar kur.

## Marka notu

Simge, Xiaomi Wear / Mi Fitness turuncu halka görselinin vektör uyarlamasıdır.
Bu bağımsız topluluk modülü resmî Xiaomi yazılımı değildir.
