# MiHealth Alias v2.1.0 — API 102

İlk GitHub Releases dağıtımı. Xiaomi Wear / Mi Fitness turuncu halkalı uygulama simgesi ve modern Android adaptif simgesi eklendi.

- **Xposed API 102:** `io.github.libxposed:api:102.0.0`; eski API 93 arayüzü kullanılmaz.
- **App Vault kapsamı:** `com.miui.personalassistant` dışında hook yok.
- **Paket takma adı:** `com.xiaomi.wearable` yalnızca widget seçicisinde `com.mi.health` olarak gösterilir.
- **Yeni ikon:** Xiaomi Wear / Mi Fitness turuncu halkalı görünümünün vektör uyarlaması.
- **Kontroller:** JUnit testleri, APK manifest paket/sürüm/simge kontrolü ve libxposed metadata kontrolü.

### Kurulum
1. LSPosed sürümünün **libxposed API 102** desteğini doğrula.
2. Buradaki APK'yı yükle, LSPosed'den MiHealth Alias modülünü etkinleştir.
3. Kapsamda yalnızca **Kişisel Asistan / App Vault**'u (`com.miui.personalassistant`) seç.
4. App Vault'u yeniden başlat; widget seçicisini kontrol et.

**Önemli:** Bu APK otomatik CI'da **debug anahtarıyla imzalanmıştır**. Eski APK farklı bir anahtarla imzalanmışsa, `INSTALL_FAILED_UPDATE_INCOMPATIBLE` alınabilir; önce eski **MiHealth Alias modülünü** kaldır. Mi Fitness'ın kendisini kaldırma.

**Doğrulama sınırı:** GitHub Actions derlemesi/testleri başarılı olsa bile gerçek telefon/HyperOS sürümünde widget görünürlüğü ayrıca sınanmalıdır. Bu proje bağımsız topluluk modülüdür, Xiaomi'nin resmî uygulaması değildir. Mi Fitness/Xiaomi Wear markası Xiaomi'ye aittir.
