package com.paytrek.stater

import android.nfc.tech.IsoDep
import android.nfc.TagLostException
import android.util.Log
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException

class IsoDepProvider(val tag: IsoDep): IProvider {
    override fun transceive(command: ByteArray): ByteArray {
        try {
            return tag.transceive(command)
        } catch(e: TagLostException) {
            Log.d("ERROR", "Tag was lost")
            
            return byteArrayOf()
        } catch(e: IOException) {
            e.printStackTrace()

            return byteArrayOf()
        }
    }

    override fun getAt(): ByteArray {
        return tag.getHistoricalBytes()
    }
} 