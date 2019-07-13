package io.github.msh91.arch.util.providers.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class FileProvider @Inject constructor(private val context: Context) : BaseFileProvider {
    override fun getUriForFile(context: Context, authority: String, file: File): Uri {
        return FileProvider.getUriForFile(context, authority, file)
    }

    override fun makeShareCacheDirectory(): File {
        return File(context.cacheDir, "/image").also {
            if (it.exists()) it.delete()
            it.mkdir()
        }
    }

    override fun writeBitmapToFile(parent: File, bitmap: Bitmap, fileName: String): File {
        return File(parent, fileName).also {
            val out = it.outputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()
        }
    }

    override fun getByteArrayFromInputStream(inputStream: InputStream): ByteArray {
        return ByteArray(inputStream.available()).also {
            inputStream.read(it)
            inputStream.close()
        }
    }
}