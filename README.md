# KeyMapper

## Bu proje ne yapar?
- Fiziksel tuşları (ses tuşları vb.) yakalayıp istediğin aksiyona (uygulama aç, geri git,
  ana ekran, bildirimler, URL aç...) yönlendiren bir AccessibilityService içerir.
- **Admin Panel** (MainActivity): eklediğin tüm config'leri (tuş eşlemelerini) listeler,
  aç/kapat, düzenle, sil imkanı verir.
- Configler cihazda JSON olarak saklanır (SharedPreferences) 

## APK'yı nasıl üretirim?
1. [Android Studio](https://developer.android.com/studio) indir ve kur (ücretsiz).
2. "Open" ile bu klasörü (KeyMapperApp) aç. Gradle senkronizasyonunu bekle

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
- ACIK KAYNAKDIR KENDINIZ TASALAYABILIRSIN BAZI KEYMAPPER YAKALAYAN UYGULUMALAR BUNU YAKALMAYAZ
EN
KeyMapper
What does this project do?
Includes an AccessibilityService that captures physical button presses (such as the volume buttons) and maps them to custom actions like:
Launch an app
Go back
Return to the Home screen
Open the notification panel
Open a URL
And more
Admin Panel (MainActivity): Lets you view all created key mappings, enable/disable them, edit them, or delete them.
Configurations are stored locally on the device as JSON using SharedPreferences.
How to build the APK
Download and install Android Studio (free): https://developer.android.com/studio
Open the KeyMapperApp project in Android Studio and wait for Gradle synchronization to finish.
From the top menu, select Build > Build Bundle(s) / APK(s) > Build APK(s).
When the build is complete, click Locate in the notification to find the generated APK (usually located at app/build/outputs/apk/debug/app-debug.apk).
Transfer the APK to your Android device, enable Install from Unknown Sources if necessary, and install it.
After installation
Open the app to access the Admin Panel.
Tap Open Accessibility Settings, find KeyMapper Panel in the list, and enable it.
Android will display a security warning. Tap Allow—this is required for the app to detect hardware button presses.
Return to the Admin Panel and tap the + button.
Tap Start Key Capture, then press the physical button you want to map (for example, Volume Up).
Choose an action (for example, Launch App) and, if required, enter the target package name (e.g. com.spotify.music).
Save the configuration. From now on, pressing that hardware button will trigger the selected action.
Notes / Limitations
Some hardware buttons (especially the Power button and certain manufacturer-specific buttons) cannot be intercepted due to Android security restrictions. Root access may be required for those buttons.
This project is open source, so you are free to modify and customize it to suit your needs.
Please note that some key-mapping applications may prevent this app from detecting button presses if they are already intercepting the same hardware keys.
