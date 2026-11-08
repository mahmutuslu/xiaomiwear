# Değişiklik günlüğü

## 2.1.0 — Nihai paketleme ve Xiaomi Wear simgesi
- Android uygulama simgesi Xiaomi Wear / Mi Fitness turuncu halka görünümüne uyarlandı.
- Android 8+ adaptif ikon ile eski tip vektör simgesi eklendi.
- Sürüm adı `2.1.0`, sürüm kodu `210` oldu.
- GitHub Releases için doğrulama sonrasında otomatik APK dağıtımı eklendi.
- API 102 modül giriş/kapsamı ve mevcut hook davranışı değişmeden korundu.

## 2.0.0 — libxposed API 102
- Legacy XposedBridge / API 93 modül girişi kaldırıldı.
- Modern libxposed 102 üzerinden App Vault'a sınırlı hook sistemi yazıldı.
- META-INF/Xposed tanımları, sabit kapsam ve hook kimlikleri eklendi.
- Paket takma adı dönüşüm testleri ve APK metadata doğrulaması eklendi.
- Minimum Android API ve paket kimliği korunarak güncelleme yolu açık tutuldu.

**Doğrulama:** CI'da derleme, manifest metadata ve birim testleri; gerçek
HyperOS App Vault davranışı cihaz üzerinde ayrıca kontrol edilmelidir.
