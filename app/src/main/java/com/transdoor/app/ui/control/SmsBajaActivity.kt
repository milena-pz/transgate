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

class SmsBajaActivity : AppCompatActivity() {

    // Declaramos las vistas que usaremos
    private lateinit var tvNombreBaja: TextView
    private lateinit var tvTelefonoBaja: TextView
    private lateinit var tvTipoUsuarioSMS: TextView
    private lateinit var tvMensajeSMSBaja: TextView // Muestra el mensaje que se enviará por SMS
    private lateinit var btnEnviarSMSBaja: Button // Botón para enviar el SMS
    private lateinit var usuario: Usuario // Objeto Usuario recibido desde la actividad anterior

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_baja) // Establecemos el layout para la actividad

        // Inicializamos las vistas
        tvNombreBaja = findViewById(R.id.tvNombreBaja)
        tvTelefonoBaja = findViewById(R.id.tvTelefonoBaja)
        tvTipoUsuarioSMS = findViewById(R.id.tvTipoUsuarioSMS)
        tvMensajeSMSBaja = findViewById(R.id.tvMensajeSMSBaja)
        btnEnviarSMSBaja = findViewById(R.id.btnEnviarSMSBaja)

        // Recibimos el objeto 'usuario' que fue enviado desde la actividad anterior

        @Suppress("DEPRECATION")
         usuario = intent.getSerializableExtra("usuario") as? Usuario ?: run {
            Toast.makeText(this, "Error al recibir el usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Generamos el mensaje de baja que será enviado por SMS
        val telefonoEnvio = "693486292" // Número fijo al que se enviará el SMS
        val mensaje = "2003 UR.R.U ${usuario.telefono}" // Mensaje que contiene el teléfono del usuario dado de baja

        // Asignar valores dinámicos, y mostramos
        tvNombreBaja.text = getString(R.string.nombreSBaja, usuario.nombre_completo)
        tvTelefonoBaja.text =   telefonoEnvio
        tvTipoUsuarioSMS.text = getString(R.string.tipoDeusuarioB, usuario.tipo_usuario)
        tvMensajeSMSBaja.text = mensaje


        val btnCancelar = findViewById<Button>(R.id.btnCancelarSMSBaja)
        btnCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish() // ← Vuelve a VerUsuarioActivity
        }







        // Configuramos el botón para enviar el SMS
        btnEnviarSMSBaja.setOnClickListener {
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
                finish() // Cierra la actividad actual y vuelve a la lista de usuarios
            } catch (e: Exception) {
                Log.e("SMS", "Error al abrir app de SMS", e)
                // Si ocurre algún error al intentar abrir la app de SMS
                Toast.makeText(this, "No se pudo abrir la app de SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
