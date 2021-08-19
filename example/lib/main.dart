import 'package:flutter/material.dart';
import 'package:emv_card_reader/emv_card_reader.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _emv = EmvCardReader();

  @override
  void initState() {
    super.initState();

    final ac = (bool status) {
      // availability status
      if (!status) {
        return;
      }

      _emv.read().then((value) => print(value));
    };

    final sc = (_) {
      _emv.available().then(ac);
    };

    _emv.start().then(sc);
  }

  @override
  void dispose() {
    _emv.stop();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(),
      ),
    );
  }
}
