## EMV card reader Flutter plugin

This plugin reads payment related cards by using NFC technology. Only Android supported. Apple's CoreNFC does not support 
payment related cards except ApplePay application. Please check [CoreNFC](https://developer.apple.com/documentation/corenfc?changes=_7) for more.

### Install

Add dependency in `pubsec.yaml`,

```yaml
dependencies:
    emv_card_reader:
        git:
            url: git://github.com/trk54ylmz/emv_card_reader.git
```

and add permission in `AndroidManifest.xml`,

```xml
<uses-permission android:name="android.permission.NFC"/>
```

### Usage

```dart
final emv = EmvCardReader();

final s = await emv.start();

final a = await emv.available();

if (a) {
    final c = await emv.read();

    print(c);
}
```
