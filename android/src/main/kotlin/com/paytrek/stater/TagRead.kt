package com.paytrek.stater

import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Handler
import android.os.Looper
import android.util.Log

public fun Tag.read(callback: (Map<*, *>) -> Unit) {
    val ndef = Ndef.get(this)
    ndef.connect()

    val ndefMessage = ndef.ndefMessage ?: ndef.cachedNdefMessage
    val message = ndefMessage.toByteArray().toString(Charsets.UTF_8)
    val id = id.bytesToHexString()

    ndef.close()

    val data = mapOf("id" to id, "message" to message, "error" to "", "status" to "reading")
    val mainHandler = Handler(Looper.getMainLooper())

    mainHandler.post {
        callback(data)
    }
}

public fun ByteArray.bytesToHexString(): String? {
    val stringBuilder = StringBuilder("0x")

    for (i in indices) {
        stringBuilder.append(Character.forDigit(get(i).toInt() ushr 4 and 0x0F, 16))
        stringBuilder.append(Character.forDigit(get(i).toInt() and 0x0F, 16))
    }

    return stringBuilder.toString()
}
