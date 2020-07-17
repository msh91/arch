package io.github.msh91.arch.data.source.cloud

import com.google.gson.Gson
import com.google.gson.JsonParseException
import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.source.remote.CryptoDataSource
import io.github.msh91.arch.data.source.remote.model.ResponseWrapperDto
import io.github.msh91.arch.data.util.fromJson

/**
 * Stub implementation of [CryptoDataSource].
 *
 * <b>CAUTION : this implementation should be used only in debug mode</b>
 */
class StubCryptoDataSource(
    private val gson: Gson,
    private val fileProvider: BaseFileProvider
) : CryptoDataSource {

    override suspend fun getLatestUpdates(
        start: Int,
        limit: Int,
        convertTo: String
    ): ResponseWrapperDto<List<CryptoCurrency>> {
        val inputStream = fileProvider.getAsset("latest_updates.json")
        val response = String(fileProvider.getByteArrayFromInputStream(inputStream))
        return gson.fromJson(response) ?: throw JsonParseException("provided json file is not ")
    }
}
