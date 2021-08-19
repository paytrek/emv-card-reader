#import "EmvCardReaderPlugin.h"
#if __has_include(<emv_card_reader/emv_card_reader-Swift.h>)
#import <emv_card_reader/emv_card_reader-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "emv_card_reader-Swift.h"
#endif

@implementation EmvCardReaderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftEmvCardReaderPlugin registerWithRegistrar:registrar];
}
@end
