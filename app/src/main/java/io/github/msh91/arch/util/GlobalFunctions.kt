package io.github.msh91.arch.util

import java.io.InputStream
import javax.inject.Inject

/**
 * Some global functions will be defined in this class
 */
class GlobalFunctions @Inject constructor() {

    /**
     * get [ByteArray] from given [InputStream] instance
     */
    fun getByteArrayFromInputStream(inputStream: InputStream): ByteArray {
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return buffer
    }
}
