package com.c242ps188.mentally_app.data.remote

import com.c242ps188.mentally_app.data.local.model.MentalHealthRequest
import com.c242ps188.mentally_app.data.remote.response.DiagnoseResponse
import com.c242ps188.mentally_app.data.remote.response.LoginResponse
import com.c242ps188.mentally_app.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("v1/auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("v1/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ):LoginResponse

    @POST("v1/diagnosis")
    suspend fun submitMentalHealthData(
        @Body requestBody: MentalHealthRequest
    ): DiagnoseResponse

}