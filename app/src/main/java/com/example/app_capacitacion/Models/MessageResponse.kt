package com.example.app_capacitacion.Models


data class MessageResponse(val message: String){
    data class ForgotPasswordResponse(val message: String? = null, val errors: Map<String, List<String>>? = null)

    data class ResetPasswordResponse(val message: String? = null, val errors: Map<String, List<String>>? = null)

}


