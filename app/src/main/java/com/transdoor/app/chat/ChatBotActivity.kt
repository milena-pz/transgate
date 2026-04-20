package com.transdoor.app.chat


import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.transdoor.app.R


class ChatBotActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var editMensaje: EditText
    private lateinit var btnEnviar: ImageButton
    private lateinit var btnSalir: ImageButton


    private val mensajes = mutableListOf<Mensaje>() // Lista de mensajes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot) // Layout principal de la pantalla del chat

        // Vinculamos vistas con XML
        recyclerView = findViewById(R.id.recyclerMensajes)
        editMensaje = findViewById(R.id.editMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)
        btnSalir = findViewById(R.id.btnSalir)

        // Configuramos el RecyclerView
        adapter = ChatAdapter(mensajes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        // Configurar RecyclerView
        adapter = ChatAdapter(mensajes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // ✅ Mensaje de bienvenida automático
        agregarMensaje(Mensaje("Hola 👋 soy MIA. ¿Con qué puedo ayudarte?", esUsuario = false))





        // Enviar mensaje del usuario
        btnEnviar.setOnClickListener {
            val textoUsuario = editMensaje.text.toString().trim()
            if (textoUsuario.isNotEmpty()) {
                agregarMensaje(Mensaje(textoUsuario, esUsuario = true))
                val respuestaBot = ChatBot.responder(textoUsuario)
                agregarMensaje(respuestaBot)
                editMensaje.text.clear()
            }
        }
        //salimos del chat
        btnSalir.setOnClickListener {
            finish()
        }

}
    fun agregarMensaje(mensaje: Mensaje) {
        mensajes.add(mensaje)
        adapter.notifyItemInserted(mensajes.size - 1)
        recyclerView.scrollToPosition(mensajes.size - 1)
    }



}

