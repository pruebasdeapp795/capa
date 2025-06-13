package com.example.app_capacitacion

import com.example.app_capacitacion.Models.Capacitante
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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Path
import retrofit2.http.Url


interface ApiServices {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("capacitantes/{numero_documento}")
    fun getCapacitante(@Path("numero_documento") doc: String): Call<Capacitante>

    @GET
    fun getCapacitanteFromUrl(@Url fullUrl: String): Call<Capacitante>

    @GET("user")
    fun getUser(@Header("Authorization") authToken: String): Call<LoginResponse.User>

    @POST("logout")
    fun logout(@Header("Authorization") authToken: String): Call<GeneralResponse>

}