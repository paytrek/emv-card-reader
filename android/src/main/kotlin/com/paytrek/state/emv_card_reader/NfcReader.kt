package com.paytrek.state.emv_card_reader

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Handler
import android.os.Looper
import com.github.devnied.emvnfccard.parser.EmvTemplate
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import android.util.Log

sealed class AbstractNfcHandler(val result: MethodChannel.Result, val call: MethodCall) : NfcAdapter.ReaderCallback {
    protected fun unregister() = EmvCardReaderPlugin.listeners.remove(this)
}

class NfcReader(result: MethodChannel.Result, call: MethodCall) : AbstractNfcHandler(result, call) {
    private val handler = Handler(Looper.getMainLooper())

    override fun onTagDiscovered(tag: Tag) {
        val id = IsoDep.get(tag)
        id.connect()

        val provider = IsoDepProvider(id)
        val config = EmvTemplate.Config()

        val parser = EmvTemplate.Builder().setProvider(provider).setConfig(config).build()

        val card = parser.readEmvCard()

        var number: String? = null
        var expire: String? = null
        var holder: String? = null

        val fmt = SimpleDateFormat("MM/YY")

        if (card.track1 != null) {
            number = card.track1.cardNumber
            expire = fmt.format(card.track1.expireDate)
        } else {
            number = card.track2.cardNumber
            expire = fmt.format(card.track2.expireDate)
        }

        if (card.holderFirstname != null && card.holderLastname != null) {
            holder = card.holderFirstname + " " + card.holderLastname
        }

        val res = HashMap<String, String?>()
        res.put("type", card.type.name)
        res.put("number", number)
        res.put("expire", expire)
        res.put("holder", holder)

        handler.post{ result.success(res) }

        unregister()
    }
}