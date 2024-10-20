package com.example.nfccardreader.helper_classes

import android.nfc.tech.IsoDep
import android.util.Log
import com.example.nfccardreader.data.entities.CardResult
import com.example.nfccardreader.data.entities.CardType
import java.io.IOException
import java.util.Arrays

class CardReaderHelper {
    val newWaterCardSelectAppletData = byteArrayOf(
        0x45.toByte(),
        0x47.toByte(),
        0x59.toByte(),
        0x48.toByte(),
        0x43.toByte(),
        0x57.toByte(),
        0x57.toByte(),
    )
    val newWaterCardSelectWaFileData = byteArrayOf(
        0x57.toByte(),
        0x41.toByte(),
    )
    val oldWaterCardSelectAppletData = byteArrayOf(
        0x31.toByte(),
        0x50.toByte(),
        0x41.toByte(),
        0x59.toByte(),
        0x2E.toByte(),
        0x53.toByte(),
        0x59.toByte(),
        0x53.toByte(),
        0x2E.toByte(),
        0x44.toByte(),
        0x44.toByte(),
        0x46.toByte(),
        0x30.toByte(),
        0x31.toByte(),
    )
    val elecSelectFileOneData = byteArrayOf(
        0x3F.toByte(),
        0x00.toByte(),
    )
    val elecSelectFileTwoData = byteArrayOf(
        0x30.toByte(),
        0x40.toByte(),
    )
    val unifiedElecCardSelectAppletData = byteArrayOf(
        0xAE.toByte(),
        0x02.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
    )

    fun newWaterCardFunCall(isoDep: IsoDep): CardResult {
        //Select applet
        val selectAppletResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            newWaterCardSelectAppletData,
            null
        )
        val stringRep = Arrays.toString(selectAppletResponse)
        Log.d("Select applet from new water", stringRep)
        //Select WA file
        val selectWaFileResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            newWaterCardSelectWaFileData,
            null
        )
        val stringRep2 = Arrays.toString(selectWaFileResponse)
        Log.d("Select WA file from new water", stringRep2)
        val byteArrayOne = stringRep.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val byteArrayTwo = stringRep2.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val success =
            byteArrayOne.size >= 2 && byteArrayTwo.size >= 2 && byteArrayOne[byteArrayOne.size - 2] == "-112" && byteArrayOne[byteArrayOne.size - 1] == "0" && byteArrayTwo[byteArrayTwo.size - 2] == "-112" && byteArrayTwo[byteArrayTwo.size - 1] == "0"
        var type = CardType.NOT_SPECIFIED
        if (success) {
            Log.d("Success", "Success New Water")
            type = CardType.NEW_WATER
        } else {
            Log.d("Success", "Failure New Water")
        }
        val serial = getCardSerial(isoDep)
        val buffer = fromBytesToHex(getCardBuffer(isoDep))
        return CardResult(true, serial, type, buffer)

    }

    fun oldWaterCardFunCall(isoDep: IsoDep): CardResult {
        //Select applet
        val sendAppletResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            oldWaterCardSelectAppletData,
            null
        )
        val stringRep = Arrays.toString(sendAppletResponse)
        Log.d("Select applet from old water", stringRep)

        val byteArrayOne = stringRep.replace("[", "").replace("]", "").replace(" ", "").split(",")
        Log.d("Old array", byteArrayOne[byteArrayOne.size - 2])
        Log.d("Old array", byteArrayOne[byteArrayOne.size - 1])
        val success =
            byteArrayOne.size >= 2 && byteArrayOne[byteArrayOne.size - 2] == "-112" && byteArrayOne[byteArrayOne.size - 1] == "0"
        var type = CardType.NOT_SPECIFIED
        if (success) {
            Log.d("Success", "Success Old Water")
            type = CardType.OLD_WATER
        } else {
            Log.d("Success", "Failure Old Water")
        }
        val serial = getCardSerial(isoDep)
        val buffer = fromBytesToHex(getCardBuffer(isoDep))

        return CardResult(true, serial, type, buffer)
    }

    fun unifiedElecCardFunCall(isoDep: IsoDep)
            : CardResult {
        //Select applet
        val selectAppletReponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            unifiedElecCardSelectAppletData,
            null
        )
        val stringRep = Arrays.toString(selectAppletReponse)
        Log.d("Select applet from unified elec", stringRep)
        //Select file one
        val selectFileOneResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            elecSelectFileOneData,
            null
        )
        val stringRep2 = Arrays.toString(selectFileOneResponse)
        Log.d("Select file one from unified elec", stringRep2)
        //Select file two
        val selectFileTwoResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            elecSelectFileTwoData,
            null
        )
        val stringRep3 = Arrays.toString(selectFileTwoResponse)
        Log.d("Select file two from unified elec", stringRep3)
//        Toast.makeText(this, "Select file two: $stringRep3", Toast.LENGTH_LONG).show()

        val byteArrayOne = stringRep.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val byteArrayTwo = stringRep2.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val byteArrayThree =
            stringRep3.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val success =
            byteArrayOne.size >= 2 && byteArrayTwo.size >= 2 && byteArrayThree.size >= 2 && byteArrayOne[byteArrayOne.size - 2] == "-112" && byteArrayOne[byteArrayOne.size - 1] == "0" && byteArrayTwo[byteArrayTwo.size - 2] == "-112" && byteArrayTwo[byteArrayTwo.size - 1] == "0" && byteArrayThree[byteArrayThree.size - 2] == "-112" && byteArrayThree[byteArrayThree.size - 1] == "0"
        var type = CardType.NOT_SPECIFIED
        if (success) {
            Log.d("Success", "Success Unified Elec")
            type = CardType.UNIFIED_ELECTRIC
        } else {
            Log.d("Success", "Failure Unified Elec")
        }
        val serial = getCardSerial(isoDep)
        val buffer = fromBytesToHex(getCardBuffer(isoDep))
        return CardResult(true, serial, type, buffer)
    }

    fun oldElecCardFunCall(isoDep: IsoDep): CardResult {
        //Select file one
        val selectFileOneResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            elecSelectFileOneData,
            null
        )
        val stringRep = Arrays.toString(selectFileOneResponse)
        Log.d("Select file one from old elec", stringRep)
        //Select file two
        val selectFileTwoResponse = sendApduCommand(
            isoDep,
            0x00.toByte(),
            0xA4.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            elecSelectFileTwoData,
            null
        )
        val stringRep2 = Arrays.toString(selectFileTwoResponse)
        Log.d("Select file two from old elec", stringRep2)
        val byteArrayOne = stringRep.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val byteArrayTwo = stringRep2.replace("[", "").replace("]", "").replace(" ", "").split(",")
        val success =
            byteArrayOne.size >= 2 && byteArrayTwo.size >= 2 && byteArrayOne[byteArrayOne.size - 2] == "-112" && byteArrayOne[byteArrayOne.size - 1] == "0" && byteArrayTwo[byteArrayTwo.size - 2] == "-112" && byteArrayTwo[byteArrayTwo.size - 1] == "0"
        var type = CardType.NOT_SPECIFIED
        if (success) {
            Log.d("Success", "Success Old Elec")
            type = CardType.OLD_ELECTRIC
        } else {
            Log.d("Success", "Failure Old Elec")
        }
        val serial = getCardSerial(isoDep)
        val buffer = fromBytesToHex(getCardBuffer(isoDep))
        return CardResult(true, serial, type, buffer)
    }


    fun sendApduCommand(
        isoDep: IsoDep,
        cla: Byte,
        ins: Byte,
        p1: Byte,
        p2: Byte,
        data: ByteArray? = null,
        le: Int? = null
    ): ByteArray? {
        // Prepare the APDU command
        val lc = data?.size?.toByte() ?: 0x00
        val leByte = le?.toByte() ?: 0x00

        // Create the APDU command array
        val apdu: ByteArray = if (data != null) {
            if (le != null) {
                // APDU with data and Le
                byteArrayOf(cla, ins, p1, p2, lc) + data + leByte
            } else {
                // APDU with data, no Le
                byteArrayOf(cla, ins, p1, p2, lc) + data
            }
        } else {
            if (le != null) {
                // APDU without data, with Le
                byteArrayOf(cla, ins, p1, p2, lc, leByte)
            } else {
                // APDU without data and without Le
                byteArrayOf(cla, ins, p1, p2, lc)
            }
        }

        try {
            // Send the APDU command and receive the response
            val response: ByteArray = isoDep.transceive(apdu)
            return response
        } catch (e: IOException) {
            // Handle exception if the transceive fails
            e.printStackTrace()
        }
        return null
    }

    fun fromBytesToHex(bytes: ByteArray) =
        bytes.joinToString(separator = " ") { String.format("%02X", it) }

    fun fromHexToBytes(hex: String): ByteArray {
        val value = hex.replace(" ", "")
        Log.d("Hex", value)

        return value.toByteArray();
    }

    fun getCardSerial(isoDep: IsoDep): String {
        val historicalBytes = isoDep.historicalBytes
        if (historicalBytes != null) {
            Log.d("Bytes before", historicalBytes.toString())
            val hexString = fromBytesToHex(historicalBytes)
            return hexString;
        } else {
            return "Couldn't get serial"
        }
    }

    fun getCardBuffer(isoDep: IsoDep): ByteArray {
        // Initialize an empty ByteArray to store the full buffer
        val bufferList = mutableListOf<Byte>()

        // Loop through the 20 sectors
        for (i in 0 until 20) {
            // APDU command parameters
            val cla: Byte = 0x00
            val ins: Byte = 0xB0.toByte()
            val p1: Byte = i.toByte() // sector index
            val p2: Byte = 0x40 // fixed value
            val le: Int = 0x40 // Length of the expected response (64 bytes)

            // Send APDU command to read each sector
            val sectorData: ByteArray? = sendApduCommand(isoDep, cla, ins, p1, p2, le = le)
            // If the response is not null, add it to the buffer list
            if (sectorData != null ) {
                bufferList.addAll(sectorData.toList()) // Add the 64-byte sector data
            } else {
                // Handle the case where the data is invalid (e.g., sector not readable)
                println("Error reading sector $i or invalid sector size")
            }
        }

        // Convert the MutableList of Bytes to ByteArray and return the full buffer
        return bufferList.toByteArray()
    }


}