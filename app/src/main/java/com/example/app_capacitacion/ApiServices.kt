package com.example.app_capacitacion

import com.example.app_capacitacion.Models.GeneralResponse
import com.example.app_capacitacion.Models.LoginRequest
import com.example.app_capacitacion.Models.LoginResponse
import com.example.app_capacitacion.Models.MessageResponse.ForgotPasswordResponse
import com.example.app_capacitacion.Models.MessageResponse.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.app_capacitacion.Models.ModeloDatos
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded


interface ApiServices {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("user")
    fun getUser(@Header("Authorization") authToken: String): Call<LoginResponse.User>

    @POST("logout")
    fun logout(@Header("Authorization") authToken: String): Call<GeneralResponse>

    @FormUrlEncoded
    @POST("forgot-password")
    fun forgotPassword(@Field("email") email: String): Call<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("reset-password")
    fun resetPassword(
        @Field("token") token: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String
    ): Call<ResetPasswordResponse>


    @GET("cursantes")
    fun obtenerDatosTabla(): Call<List<ModeloDatos>>


}