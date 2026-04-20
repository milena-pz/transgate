package com.transdoor.app.ui.control

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.R
import com.transdoor.app.model.Usuario
import androidx.core.net.toUri

class SmsAltaActivity : AppCompatActivity() {

    // Declaramos las vistas que usaremos
    private lateinit var tvNombreAlta: TextView
    private lateinit var tvTelefonoAlta: TextView
    private lateinit var tvMensajeSMS: TextView // Muestra el mensaje que se enviará por SMS
    private lateinit var btnEnviarSMS: Button // Botón para enviar el SMS
    private lateinit var btnCancelar: Button
    private lateinit var usuario: Usuario // Objeto Usuario recibido desde la actividad anterior

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_alta) // Establecemos el layout para la actividad

        // Inicializamos las vistas
        tvMensajeSMS = findViewById(R.id.tvMensajeSMS)
        btnEnviarSMS = findViewById(R.id.btnEnviarSMS)
        btnCancelar = findViewById<Button>(R.id.btnCancelarSMSAlta)
        tvNombreAlta = findViewById(R.id.tvNombreAlta)
        tvTelefonoAlta = findViewById(R.id.tvTelefonoAlta)

        // Recibimos el objeto 'usuario' que fue enviado desde la actividad anterior
        @Suppress("DEPRECATION")
         usuario = intent.getSerializableExtra("usuario") as? Usuario ?: run {
            Toast.makeText(this, "Error al recibir el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // Generamos el mensaje de alta que será enviado por SMS
        val telefonoEnvio = "693486292" // Número fijo al que se enviará el SMS
        val mensaje = "2003 UR.S.U ${usuario.telefono}" // Mensaje que contiene el teléfono del usuario recién insertado


        // Asignar valores dinámicos,y monstramos
        tvNombreAlta.text = getString(R.string.nombreSMS, usuario.nombre_completo)
        tvTelefonoAlta.text = getString(R.string.tel_fonoSMs, usuario.telefono)
        tvMensajeSMS.text =  mensaje




        btnCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish() // ← Vuelve a la pantalla anterior (InsertarUsuarioActivity o quien lo haya lanzado)
        }





        // Configuramos el botón para enviar el SMS
        btnEnviarSMS.setOnClickListener {
            // Creamos el intent para abrir la aplicación de SMS
            val smsIntent = Intent(Intent.ACTION_VIEW)

            // Establecemos la URI para el envío del SMS (esto abre la aplicación de SMS)
            smsIntent.data = "smsto:$telefonoEnvio".toUri() // Se establece el número de teléfono al que se enviará el SMS

            // Añadimos el cuerpo del mensaje
            smsIntent.putExtra("sms_body", mensaje)

            try {
                // Intentamos abrir la aplicación de SMS
                startActivity(smsIntent)

                // Después de enviar el SMS, regresamos a la actividad anterior (ListaUsuariosActivity)
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish() // Cierra la actividad actual y vuelve a la actividad anterior (ListaUsuariosActivity)
            } catch (e: Exception) {
                Log.e("SMS", "Error al intentar abrir app de SMS", e)
                // Si ocurre algún error al intentar abrir la app de SMS
                Toast.makeText(this, "No se pudo abrir la app de SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
