package com.transdoor.app.network



import com.transdoor.app.model.Usuario

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    // Este endpoint llama al archivo PHP que devuelve la lista de usuarios
    @GET("api_listar_usuarios.php")
    fun listarUsuarios(): Call<List<Usuario>>



    // Nuevo endpoint para insertar un usuario
    @POST("api_insertar_usuario.php") // Cambia la URL por la ruta correcta en tu servidor
    fun insertarUsuario(@Body usuario: Usuario): Call<Void>



    @POST("api_editar_usuario.php")
    fun editarUsuario(@Body usuario: Usuario): Call<Void>




}
