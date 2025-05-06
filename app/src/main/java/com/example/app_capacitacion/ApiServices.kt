package com.example.app_capacitacion

import com.example.app_capacitacion.Models.GeneralResponse
import com.example.app_capacitacion.Models.LoginRequest
import com.example.app_capacitacion.Models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.app_capacitacion.Models.ModeloDatos


interface ApiServices {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("user")
    fun getUser(@Header("Authorization") authToken: String): Call<LoginResponse.User>

    @POST("logout")
    fun logout(@Header("Authorization") authToken: String): Call<GeneralResponse>



    @GET("cursantes")
    fun obtenerDatosTabla(): Call<List<ModeloDatos>>


}