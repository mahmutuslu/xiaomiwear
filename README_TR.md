# MiHealth Alias — libxposed API 102

Mi Fitness (`com.xiaomi.wearable`) uygulamasını **yalnızca HyperOS App Vault widget seçicisine**
(`com.miui.personalassistant`) Mi Health (`com.mi.health`) takma adıyla gösteren
LSPosed modülü. Mi Fitness APK'sını, eşleşmiş saati veya sistem paketlerini değiştirmez.

## Sürüm 2.0.0: gerçek API 102 geçişi

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

GitHub **Actions → Build module → son başarılı çalışma → Artifacts →
MiHealthAlias-API102-debug** ZIP'ini indirip içindeki `app-debug.apk` dosyasını kur.
İlk kurulum için debug imzalı APK oluşturulur; dağıtım için kişisel release imzası
gerekmektedir. Kaynak koddan derleme:

```bash
gradle :app:testDebugUnitTest :app:assembleDebug --no-daemon
```

## Kurulum / kullanım

1. Resmî Mi Fitness (`com.xiaomi.wearable`) kurulu olsun. Sahte `com.mi.health` APK'sı yükleme.
2. API 102 uyumlu LSPosed'i doğrula; varsa önceki MiHealth Alias sürümünü güncelle.
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
