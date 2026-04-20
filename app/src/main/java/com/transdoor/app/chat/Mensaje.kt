package com.transdoor.app.chat

data class Mensaje(
    val texto: String,          // Contenido del mensaje
    val esUsuario: Boolean      // true si lo escribe el usuario, false si lo escribe el bot
)
