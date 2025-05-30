package com.example.app_capacitacion.Models

import java.io.Serializable
data class Course(
    val nombre: String,
    val fecha_inicio: String,
    val duracion_meses: String,
    val fecha_final: String,
) : Serializable
