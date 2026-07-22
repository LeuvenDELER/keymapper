# KeyMapper Panel (Admin Panelli Tuş Eşleyici)

## Bu proje ne yapar?
- Fiziksel tuşları (ses tuşları vb.) yakalayıp istediğin aksiyona (uygulama aç, geri git,
  ana ekran, bildirimler, URL aç...) yönlendiren bir AccessibilityService içerir.
- **Admin Panel** (MainActivity): eklediğin tüm config'leri (tuş eşlemelerini) listeler,
  aç/kapat, düzenle, sil imkanı verir.
- Configler cihazda JSON olarak saklanır (SharedPreferences) — uygulamayı kapatıp açsan
  bile kaybolmaz.

## APK'yı nasıl üretirim?
1. [Android Studio](https://developer.android.com/studio) indir ve kur (ücretsiz).
2. "Open" ile bu klasörü (KeyMapperApp) aç. Gradle senkronizasyonunu bekle
   (internet ister, bağımlılıkları indirir).
3. Üstteki menüden **Build > Build Bundle(s) / APK(s) > Build APK(s)** seç.
4. Derleme bitince çıkan bildirimden "locate" diyerek APK dosyasını bul
   (genelde `app/build/outputs/apk/debug/app-debug.apk`).
5. Bu APK'yı telefonuna at, "Bilinmeyen kaynaklardan yükleme"yi aç, kur.

## Kurulumdan sonra
1. Uygulamayı aç → Admin Panel görünecek.
2. "Erişilebilirlik Ayarlarını Aç" butonuna bas → listede "KeyMapper Panel"i bul → etkinleştir.
   (Android bir uyarı gösterecek, "İzin Ver"e bas — bu normaldir, tuşları yakalamak için gerekli.)
3. Panele dön, sağ alttaki + butonuna bas.
4. "Tuş Yakalamayı Başlat"a bas, sonra eşlemek istediğin fiziksel tuşa (örn. ses açma) bas.
5. Aksiyon seç (örn. "Uygulama Aç"), gerekirse paket adını gir (örn. `com.spotify.music`).
6. Kaydet. Artık o tuşa bastığında seçtiğin aksiyon çalışacak.

## Notlar / Sınırlamalar
- Bazı donanım tuşları (özellikle güç tuşu, bazı üreticilerin özel tuşları) Android
  güvenlik kısıtlamaları yüzünden yakalanamaz; root gerekebilir.
- Bir uygulamanın paket adını öğrenmek için: Ayarlar > Uygulamalar > (uygulama) altındaki
  bilgiden ya da Play Store URL'sindeki `id=...` kısmından bakabilirsin.
- Bu proje Play Store'a yayınlanmak için tasarlanmadı, kişisel kullanım içindir.
