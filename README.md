## EMV card reader Flutter plugin

This plugin reads payment related cards by using NFC technology. Tested with VISA and Master Card. The plugin will return card number, expire date (MM/YY), holder name and card status (unknown, active, locked).

##### Please note

Only Android is supported. Apple CoreNFC does not support payment related cards, only Apple Pay can use NFC to extract card detail.
Please check [CoreNFC](https://developer.apple.com/documentation/corenfc) documentation for more.

### Install

Add dependency in `pubsec.yaml`,

```yaml
dependencies:
    emv_card_reader:
        git:
            url: git://github.com/paytrek/emv-card-reader.git
```

### Usage

Create NFC reader,

```dart
final emv = EmvCardReader();

final s = await emv.start();

final a = await emv.available();

if (a) {
    print('NFC is available in this device');
}
```

Read NFC data once,

```dart
final card = await emv.read();

print(card.number);
```

Stream NFC data,

```dart
emv.stream().listen((card) => print(card.number));
```

Close NFC reader,

```dart
emv.stop();
```

Please check [example Flutter app](https://github.com/paytrek/emv-card-reader/blob/master/example/lib/main.dart).
