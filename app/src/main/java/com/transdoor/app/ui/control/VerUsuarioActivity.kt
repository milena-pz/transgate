package com.transdoor.app.ui.control


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.R
import com.transdoor.app.model.Usuario
import java.io.Serializable

class VerUsuarioActivity : AppCompatActivity() {

    // Declaramos las vistas (TextView y botones)
    private lateinit var tvIdUsuario: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var tvTipoUsuario: TextView
    private lateinit var tvEmpresa: TextView
    private lateinit var tvFechaAlta: TextView
    private lateinit var tvFechaBaja: TextView
    private lateinit var tvEstado: TextView

    private lateinit var btnEditarUsuario: Button
    private lateinit var btnDarBajaUsuario: Button
    private lateinit var btnCancelarVerUsuario: Button

    // Usuario que recibimos desde la lista
    private lateinit var usuario: Usuario

    // 🚀 Nuevo: manejador para recibir resultado de EditarUsuarioActivity
    private val editarUsuarioLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Si el usuario se editó correctamente, cerramos esta actividad
            // y regresamos a ListaUsuariosActivity para que se actualice
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_usuario)

        // Asociamos las vistas con sus IDs del XML
        tvIdUsuario = findViewById(R.id.tvIdUsuario)
        tvNombre = findViewById(R.id.tvNombre)
        tvTelefono = findViewById(R.id.tvTelefono)
        tvTipoUsuario = findViewById(R.id.tvTipoUsuario)
        tvEmpresa = findViewById(R.id.tvEmpresa)
        tvFechaAlta = findViewById(R.id.tvFechaAlta)
        tvFechaBaja = findViewById(R.id.tvFechaBaja)
        tvEstado = findViewById(R.id.tvEstado)

        btnEditarUsuario = findViewById(R.id.btnEditarUsuario)
        btnDarBajaUsuario = findViewById(R.id.btnDarBajaUsuario)
        btnCancelarVerUsuario = findViewById(R.id.btnCancelarVerUsuario)

        // Recibimos el usuario enviado desde la lista
        @Suppress("DEPRECATION")
        usuario = intent.getSerializableExtra("usuario") as? Usuario
            ?: run {
                Toast.makeText(this, "No se pudo recibir el usuario", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        // Mostramos los datos del usuario en pantalla
        mostrarDatosUsuario()

        // ✅ Botón para ir a la pantalla de editar usuario y esperar resultado
        btnEditarUsuario.setOnClickListener {
            Toast.makeText(this, "Editando...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            intent.putExtra("usuario", usuario as Serializable)
            editarUsuarioLauncher.launch(intent) // ← ¡esperamos resultado!
        }

        // Botón para ir a la pantalla de dar de baja
        btnDarBajaUsuario.setOnClickListener {
            Toast.makeText(this, "Dando de baja...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BajaUsuarioActivity::class.java)
            intent.putExtra("usuario", usuario as Serializable)
            startActivity(intent)
        }

        // Botón para volver atrás (regresar a la lista)
        btnCancelarVerUsuario.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    /**
     * Función para mostrar todos los datos del usuario en la pantalla.
     */

    @SuppressLint("StringFormatMatches")
    private fun mostrarDatosUsuario() {
        tvIdUsuario.text = getString(R.string.id_ver, usuario.id_usuario)
        tvNombre.text = getString(R.string.nombreVer, usuario.nombre_completo)
        tvTelefono.text = getString(R.string.tel_fonoVer, usuario.telefono)
        tvTipoUsuario.text = getString(R.string.tipoVer, usuario.tipo_usuario)
        tvEmpresa.text = getString(R.string.empresa_aut_nomoVer, usuario.nombre_empresa_o_autonomo)
        tvFechaAlta.text = getString(R.string.fecha_de_altaVer, usuario.fecha_alta)
        tvFechaBaja.text = getString(R.string.fecha_de_bajaVer, usuario.fecha_baja ?: "—")
        tvEstado.text = getString(R.string.estadoVer, usuario.estado)
    }
}
