#import "SiftReactNative.h"
#import <Sift/Sift.h>

@implementation SiftReactNative {
    BOOL _configured;
    NSString *_pageName;
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setSiftConfig:(NSString *)accountId beaconKey:(NSString *)beaconKey disallowCollectingLocationData:
(BOOL)disallowCollectingLocationData serverUrlFormat:(NSString *)serverUrlFormat) {
    Sift *sift = [Sift sharedInstance];
    [sift setAccountId:accountId];
    [sift setBeaconKey:beaconKey];
    [sift setDisallowCollectingLocationData:disallowCollectingLocationData];
    if ([serverUrlFormat length] > 0) {
        [sift setServerUrlFormat:serverUrlFormat];
    }
    _configured = YES;
    if (_pageName.length > 0) {
        [sift setScreenTitle:_pageName];
    }
    [sift upload];
}

RCT_EXPORT_METHOD(setUserId: (NSString *)userId) {
    [[Sift sharedInstance] setUserId:userId];
}

RCT_EXPORT_METHOD(unsetUserId) {
    [[Sift sharedInstance] unsetUserId];
}

RCT_EXPORT_METHOD(upload) {
    [[Sift sharedInstance] upload];
}

RCT_EXPORT_METHOD(onHostPause) {
    [[Sift sharedInstance] pause];
}

RCT_EXPORT_METHOD(onHostResume) {
    [[Sift sharedInstance] resume];
}

RCT_EXPORT_METHOD(setPageName:(NSString *)pageName) {
    _pageName = pageName.length > 0 ? [pageName copy] : nil;
    if (!_configured) {
        return;
    }
    Sift *sift = [Sift sharedInstance];
    [sift setScreenTitle:_pageName];
    [sift collect];
}


- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeSiftReactNativeSpecJSI>(params);
}

@end
