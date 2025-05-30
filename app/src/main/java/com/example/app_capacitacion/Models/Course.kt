package com.example.app_capacitacion.Models

import java.io.Serializable
data class Course(
    val nombre: String,
    val vencimiento: String
) : Serializable
