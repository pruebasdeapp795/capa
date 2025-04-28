package com.example.app_capacitacion.Models

import java.util.Date

data class ModeloDatos(
    val cedula: String,
    val nombre_completo: String,
    val primer_nombre: String? = null,
    val segundo_nombre: String? = null,
    val primer_apellido: String? = null,
    val segundo_apellido: String? = null,
    val fecha_nacimiento: Date? = null,
    val genero: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val estado: String? = null
)
