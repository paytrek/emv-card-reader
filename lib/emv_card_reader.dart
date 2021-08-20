import 'dart:async';

import 'package:emv_card_reader/card.dart';
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

  Future<EmvCard?> read() async {
    return _mc.invokeMapMethod<String, String?>('read').then((value) => cardCallback(value));
  }

  Stream<EmvCard?> stream() {
    final sc = (e) => e == null ? null : cardCallback(Map<String, String?>.from(e));

    return _ec.receiveBroadcastStream().map(sc);
  }

  /// Create card object from result
  EmvCard? cardCallback(Map<String, String?>? event) {
    if (event == null) {
      return null;
    }

    return EmvCard(
      number: event['number'],
      type: event['type'],
      holder: event['holder'],
      expire: event['expire'],
      status: event['status'],
    );
  }
}
