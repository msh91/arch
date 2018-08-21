package io.github.msh91.arch.util.providers.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.InputStream

interface BaseFileProvider {
    fun makeShareCacheDirectory(): File

    fun writeBitmapToFile(parent: File, bitmap: Bitmap, fileName: String): File

    fun getByteArrayFromInputStream(inputStream: InputStream): ByteArray

    fun getUriForFile(context: Context, authority: String, file: File): Uri
}