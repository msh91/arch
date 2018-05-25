package io.github.msh91.arch.util

import java.io.InputStream
import javax.inject.Inject


class GlobalFunctions @Inject constructor() {
    fun checkNationalCodeValidity(nationalCode: String?): Boolean {

        var nationalCodeCumulative: Long = 0
        val nationalCodeIntArray = IntArray(10)
        val nationalCodeMod: Long
        val exceptionsNationalCode = arrayOf(
                "0000000000",
                "1111111111",
                "2222222222",
                "3333333333",
                "4444444444",
                "5555555555",
                "6666666666",
                "7777777777",
                "8888888888",
                "9999999999")

        if (nationalCode == null || nationalCode.length != 10 || exceptionsNationalCode.contains(nationalCode)) {
            return false
        }

        for (i in 0..9) {
            if (!Character.isDigit(nationalCode[i])) {
                return false
            }
            nationalCodeIntArray[i] = Integer.parseInt(nationalCode[i].toString()) * (10 - i)
        }

        for (i in 0..8) {
            nationalCodeCumulative += nationalCodeIntArray[i].toLong()
        }

        nationalCodeMod = nationalCodeCumulative % 11

        return (
                nationalCodeMod < 2
                        &&
                        nationalCodeIntArray[9].toLong() == nationalCodeMod
                        ||
                        nationalCodeMod >= 2
                        &&
                        11 - nationalCodeMod == nationalCodeIntArray[9].toLong())

    }

    fun getByteArrayFromInputStream(inputStream: InputStream): ByteArray {
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return buffer
    }
}