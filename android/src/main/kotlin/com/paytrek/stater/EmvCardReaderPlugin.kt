package com.paytrek.stater

import android.Manifest
import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.*
import android.nfc.NfcManager
import android.nfc.Tag
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.concurrent.CopyOnWriteArrayList

class EmvCardReaderPlugin: FlutterPlugin, ActivityAware, MethodCallHandler, EventChannel.StreamHandler, NfcAdapter.ReaderCallback {
  internal var sink: EventChannel.EventSink? = null

  private var activity : Activity? = null

  private var adapter : NfcAdapter? = null

  private lateinit var mc : MethodChannel

  private lateinit var ec : EventChannel

  companion object {
    internal val listeners = CopyOnWriteArrayList<NfcAdapter.ReaderCallback>()
  }

  private fun start() {
    if (adapter == null) return

    val f = FLAG_READER_NFC_A or FLAG_READER_NFC_B or FLAG_READER_NFC_F or FLAG_READER_NFC_V

    listeners.add(NfcScanner(this))

    adapter!!.enableReaderMode(activity, this, f, null)
  }

  private fun stop() {
    if (adapter == null) return

    adapter!!.disableReaderMode(activity)

    listeners.clear()
  }

  override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    mc = MethodChannel(binding.binaryMessenger, "emv_card_reader_channel")
    mc.setMethodCallHandler(this)

    ec = EventChannel(binding.binaryMessenger, "emv_card_reader_sink")
    ec.setStreamHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    require(activity != null) { "Plugin not ready yet" }

    if (call.method == "available") {
      if (adapter != null && adapter!!.isEnabled()) {
        result.success(true)
      } else {
        result.success(false)
      }
    } else if (call.method == "start") {
      start()

      result.success(true)
    } else if (call.method == "stop") {
      stop()

      result.success(true)
    } else if (call.method == "read") {
      listeners.add(NfcReader(result, call))
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    mc.setMethodCallHandler(null)
    ec.setStreamHandler(null)
  }

  override fun onAttachedToActivity(@NonNull binding: ActivityPluginBinding) {
    if (activity != null) return

    val activity = binding.activity
    this.activity = activity

    val manager = activity.getSystemService(Context.NFC_SERVICE) as NfcManager

    adapter = manager.defaultAdapter

    activity.requestPermissions(arrayOf(Manifest.permission.NFC), 1007)

    start()
  }

  override fun onTagDiscovered(tag: Tag) {
    listeners.forEach { it.onTagDiscovered(tag) }
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
    sink = events
  }

  override fun onCancel(arguments: Any?) {
    sink = null
  }

  override fun onDetachedFromActivity() {
    activity = null
  }
}
