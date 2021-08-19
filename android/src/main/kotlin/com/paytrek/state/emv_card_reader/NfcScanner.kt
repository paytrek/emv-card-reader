package com.paytrek.state.emv_card_reader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.EmvTemplate
import android.util.Log
import java.text.SimpleDateFormat

class NfcScanner(private val plugin: EmvCardReaderPlugin) : NfcAdapter.ReaderCallback {
    override fun onTagDiscovered(tag: Tag) {
        val sink = plugin.sink ?: return

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

        sink.success(res)
    }
}
