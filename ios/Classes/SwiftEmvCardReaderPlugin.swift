import Flutter
import UIKit

public class SwiftEmvCardReaderPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "emv_card_reader_channel", binaryMessenger: registrar.messenger())
    let instance = SwiftEmvCardReaderPlugin()

    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    // iOS not supported. For more,
    // https://developer.apple.com/documentation/corenfc
    result(FlutterMethodNotImplemented)
  }
}
