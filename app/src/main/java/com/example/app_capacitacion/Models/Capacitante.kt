package com.example.app_capacitacion.Models

import java.io.Serializable

data class Capacitante(
    val id: Int,
    val nombre: String?,
    val tipo_documento : String?,
    val numero_documento: String?,
    val correo: String?,
    val telefono: String?,
    val estado: String?,
    val empresa: String?,
    val nit: String?,
    val cursos: List<Curso>
) : Serializable {

    data class Curso(
        val id: Int,
        val nombre_curso: String,
        val vigencia_curso: String,
        val instructor: Instructor
    )

    data class Instructor(
        val id: Int,
        val nombre: String,
        val empresa: String
    )
}