package com.example.app_capacitacion.Models

data class LoginResponse(
    val access_token: String,
    val user: User,
    val token_type: String,
    val expires_in: Int
) {
    data class User(
        val id: Int,
        val name: String,
        val email: String,

    )

}