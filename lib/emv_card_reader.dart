import 'dart:async';

import 'package:flutter/services.dart';

class EmvCardReader {
  static const _mc = const MethodChannel('emv_card_reader_channel');

  static const _ec = const EventChannel('emv_card_reader_sink');

  Future<bool> available() async {
    return await _mc.invokeMethod('available');
  }

  Future<bool> start() async {
    return await _mc.invokeMethod('start');
  }

  Future<bool> stop() async {
    return await _mc.invokeMethod('stop');
  }

  Future<Map<String, String?>?> read() async {
    return _mc.invokeMapMethod<String, String?>('read');
  }

  Stream<Map<String, String?>?> stream() {
    return _ec.receiveBroadcastStream().map((e) => Map<String, String?>.from(e));
  }
}
