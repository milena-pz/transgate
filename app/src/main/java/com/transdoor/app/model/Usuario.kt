package com.transdoor.app.model
import java.io.Serializable

data class Usuario(
    val id_usuario: Int,
    val nombre_completo: String,
    val tipo_usuario: String,
    val nombre_empresa_o_autonomo: String?,
    val telefono: String,
    val fecha_alta: String,
    val fecha_baja: String?,
    val estado: String
): Serializable // Importante: permite pasar objetos entre actividades con intent.putExtra()