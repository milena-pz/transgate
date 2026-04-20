package com.transdoor.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.network.RetrofitClient
import com.transdoor.app.ui.control.ListaUsuariosActivity
import androidx.lifecycle.lifecycleScope
import com.transdoor.app.chat.ChatBotActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnAcceder: Button
    private lateinit var btnChatAyuda: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Referencia al botón Acceder y usuario y contraseña

        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        btnAcceder = findViewById<Button>(R.id.btnAcceder)
        btnChatAyuda = findViewById(R.id.btnChatAyuda)





        // Acción al pulsar
        btnAcceder.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Complete ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            lifecycleScope.launch {
                try {
                    val mapa = RetrofitClient.adminApiService.getAdministradores()

                    if (mapa.containsKey(usuario)) {
                        val clave = mapa[usuario]
                        if (clave == contrasena) {
                            startActivity(Intent(this@MainActivity, ListaUsuariosActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        //Accion de auyda del usuario
        btnChatAyuda.setOnClickListener {
            val intent = Intent(this@MainActivity, ChatBotActivity::class.java)
            startActivity(intent)
        }







            }
        }















