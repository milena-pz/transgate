package com.transdoor.app.ui.control

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.R
import com.transdoor.app.model.Usuario
import com.transdoor.app.network.RetrofitInstance
import java.text.SimpleDateFormat
import java.util.*

class BajaUsuarioActivity : AppCompatActivity() {

    // Declaramos las vistas
    private lateinit var tvIdUsuarioBaja: TextView
    private lateinit var tvNombreBaja: TextView
    private lateinit var tvTelefonoBaja: TextView
    private lateinit var tvTipoUsuarioBaja: TextView
    private lateinit var tvEmpresaBaja: TextView
    private lateinit var tvFechaAltaBaja: TextView
    private lateinit var tvEstadoBaja: TextView
    private lateinit var tvFechaBaja: TextView

    private lateinit var btnConfirmarBaja: Button
    private lateinit var btnCancelarBaja: Button

    private lateinit var usuario: Usuario
    private lateinit var fechaHoy: String
    private lateinit var smsLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baja_usuario)

        // Inicializamos las vistas
        tvIdUsuarioBaja = findViewById(R.id.tvIdUsuarioBaja)
        tvNombreBaja = findViewById(R.id.tvNombreBaja)
        tvTelefonoBaja = findViewById(R.id.tvTelefonoBaja)
        tvTipoUsuarioBaja = findViewById(R.id.tvTipoUsuarioBaja)
        tvEmpresaBaja = findViewById(R.id.tvEmpresaBaja)
        tvFechaAltaBaja = findViewById(R.id.tvFechaAltaBaja)
        tvEstadoBaja = findViewById(R.id.tvEstadoBaja)
        tvFechaBaja = findViewById(R.id.tvFechaBaja)

        btnConfirmarBaja = findViewById(R.id.btnConfirmarBaja)
        btnCancelarBaja = findViewById(R.id.btnCancelarBaja)

        // Obtenemos el usuario enviado desde VerUsuarioActivity

        @Suppress("DEPRECATION")
        usuario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usuario", Usuario::class.java)
        } else {
            intent.getSerializableExtra("usuario") as? Usuario
        } ?: run {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }




        // Generar fecha de hoy para la baja
        fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

       // Actualizar el objeto usuario con baja y estado "Baja"
        usuario = usuario.copy(
            fecha_baja = fechaHoy,
            estado = "Baja"
        )



        // Mostramos los datos, incluyendo la fecha de baja actual
        mostrarDatos()

        // Preparamos launcher para esperar resultado de SMS
        smsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }
       // Confirmar baja → actualizar por API → lanzar SMS
        btnConfirmarBaja.setOnClickListener {
            RetrofitInstance.api.editarUsuario(usuario).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@BajaUsuarioActivity, SmsBajaActivity::class.java)
                        intent.putExtra("usuario", usuario)
                        smsLauncher.launch(intent)
                    } else {
                        Toast.makeText(this@BajaUsuarioActivity, "Error HTTP al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@BajaUsuarioActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }






        // Botón para cancelar → volver a lista actualizada
        btnCancelarBaja.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }


    @SuppressLint("StringFormatMatches")
    private fun mostrarDatos() {
        tvIdUsuarioBaja.text = getString(R.string.id_baja, usuario.id_usuario)
        tvNombreBaja.text = getString(R.string.nombre_baja, usuario.nombre_completo)
        tvTelefonoBaja.text = getString(R.string.tel_fono_Bja, usuario.telefono)
        tvTipoUsuarioBaja.text = getString(R.string.tipo_baja, usuario.tipo_usuario)
        tvEmpresaBaja.text =
            getString(R.string.empresa_aut_nomoBaja, usuario.nombre_empresa_o_autonomo)
        tvFechaAltaBaja.text = getString(R.string.fecha_de_altaBaja, usuario.fecha_alta)
        tvEstadoBaja.text = getString(R.string.estadoBaja, usuario.estado)
        tvFechaBaja.text = getString(R.string.fecha_de_bajaB, fechaHoy)
    }
}

