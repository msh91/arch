package io.github.msh91.arch.data.restful

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * The without-token apis should be defined here
 */
interface APIs {

    @FormUrlEncoded
    @POST("/oauth/token")
    fun refreshToken(
            @Field("refresh_token") refreshToken: String,
            @Field("grant_type") grantType: String = "refresh_token"): Call<ResponseBody>
}