# Değişiklik günlüğü

## 2.0.0 — libxposed API 102
- Legacy XposedBridge / API 93 modül girişi kaldırıldı.
- Modern libxposed 102 üzerinden App Vault'a sınırlı hook sistemi yazıldı.
- META-INF/Xposed tanımları, sabit kapsam ve hook kimlikleri eklendi.
- Paket takma adı dönüşüm testleri ve APK metadata doğrulaması eklendi.
- Minimum Android API ve paket kimliği korunarak güncelleme yolu açık tutuldu.

**Doğrulama:** CI'da derleme ve birim testleri; App Vault'un gerçek cihazdaki
davranışı ayrıca kontrol edilmelidir.
