package io.github.msh91.arch.data.source.local.file

import android.content.ContentResolver
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

    /**
     * Resolves asset name to to Inputstream
     *
     * @param assetName to  fetched from the asset
     * @return InputStream
     */
    fun getAsset(fileName: String): InputStream

    /**
     * Resolves uri to Inputstream
     *
     * @param uri
     * @return InputStream
     */
    fun getContentInputStream(uri: Uri): InputStream

    /**
     * Get MimType of given [Uri]
     */
    fun getMimType(uri: Uri): String

    /**
     * get an instance of [ContentResolver]
     */
    fun getContentResolver(): ContentResolver
}
