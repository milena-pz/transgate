package com.transdoor.app.network

import retrofit2.http.GET

// Interfaz que representa el endpoint para el JSON de administradores
interface AdminApiService {
    @GET("administradores.json") // Ruta relativa a BASE_URL
    suspend fun getAdministradores(): Map<String, String>
}
