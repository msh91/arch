package io.github.msh91.arch.data.restful

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIs {


    @POST("api/v1/users")
    fun requestUser(@Body model: TestUserModel): Flowable<TestUserModel>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun refreshToken(
            @Field("refresh_token") refreshToken: String,
            @Field("grant_type") grantType: String = "refresh_token"): Call<ResponseBody>
}