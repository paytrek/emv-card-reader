import 'package:emv_card_reader/card.dart';
import 'package:emv_card_reader/emv_card_reader.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  // Card reader instance
  final _emv = EmvCardReader();

  // Card data
  EmvCard? _card;

  @override
  void initState() {
    super.initState();

    final ac = (bool status) {
      // Availability status
      if (!status) {
        return;
      }

      // Stream NFC tags
      _emv.stream().listen((card) => setState(() => _card = card));

      // OR read once by using,
      // _emv.read().then((value) => print(value));
    };

    final sc = (_) {
      // Check availability
      _emv.available().then(ac);
    };

    // Start NFC adapter
    _emv.start().then(sc);
  }

  @override
  void dispose() {
    // Stop NFC adapter
    _emv.stop();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var result;
    if (_card == null) {
      result = Text('Waiting');
    } else {
      final number = _card!.number;
      final type = _card!.type;
      final holder = _card!.holder;
      final expire = _card!.expire;
      final status = _card!.status;

      result = Text('$number - $type - $holder - $expire - $status');
    }

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(child: result),
      ),
    );
  }
}
