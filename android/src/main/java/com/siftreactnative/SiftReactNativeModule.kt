package com.siftreactnative

import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import siftscience.android.Sift


@ReactModule(name = SiftReactNativeModule.NAME)
class SiftReactNativeModule(reactContext: ReactApplicationContext) :
  NativeSiftReactNativeSpec(reactContext), LifecycleEventListener {

  companion object {
    const val NAME = "SiftReactNative"
  }

  private var siftConfig: Sift.Config? = null
  @Volatile private var pageName: String? = null

  init {
    reactContext.addLifecycleEventListener(this)
  }

  override fun getName(): String = NAME

  @ReactMethod
  override fun setSiftConfig(
    accountId: String,
    beaconKey: String,
    disallowCollectingLocationData: Boolean,
    serverUrlFormat: String?
  ) {
    val builder = Sift.Config.Builder()
      .withAccountId(accountId)
      .withBeaconKey(beaconKey)
      .withDisallowLocationCollection(disallowCollectingLocationData)

    if (!serverUrlFormat.isNullOrEmpty()) {
      builder.withServerUrlFormat(serverUrlFormat)
    }

    siftConfig = builder.build()
    Sift.open(reactApplicationContext, siftConfig, pageName)
    Sift.collect()
  }

  @ReactMethod
  override fun setUserId(userId: String?) {
    if (!userId.isNullOrEmpty()) {
      Sift.setUserId(userId)
    }
  }

  @ReactMethod
  override fun unsetUserId() {
    Sift.unsetUserId()
  }

  @ReactMethod
  override fun upload() {
    Sift.collect()
  }

  @ReactMethod
  override fun setPageName(pageName: String?) {
    val name = if (pageName.isNullOrEmpty()) null else pageName
    this.pageName = name
    if (siftConfig == null) return
    Sift.open(reactApplicationContext, name)
    Sift.collect()
  }

  override fun onHostResume() {
    Sift.resume(reactApplicationContext, pageName)
  }

  override fun onHostPause() {
    Sift.pause()
  }

  override fun onHostDestroy() {
    Sift.close()
  }
}