package com.transdoor.app.ui.control
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.R
import com.transdoor.app.network.RetrofitInstance
import com.transdoor.app.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
private lateinit var smsLauncher: ActivityResultLauncher<Intent>


class InsertarUsuarioActivity : AppCompatActivity() {

    // Declarar los campos de la actividad
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private lateinit var spinnerTipoUsuario: Spinner
    private lateinit var etNombreEmpresa: EditText
    private lateinit var tvFechaAlta: TextView
    private lateinit var tvEstado: TextView
    private lateinit var currentDate: String


    companion object {
        private const val REQUEST_CODE_SMS = 1001 // Código para identificar la vuelta del SMS
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_usuario)

        // Inicializar los campos
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        etNombre = findViewById(R.id.etNombre)
        etTelefono = findViewById(R.id.etTelefono)
        spinnerTipoUsuario = findViewById(R.id.spinnerTipoUsuario)
        etNombreEmpresa = findViewById(R.id.etNombreEmpresa)
        tvFechaAlta = findViewById(R.id.tvFechaAlta)
        tvEstado = findViewById(R.id.tvEstado)

        // Mostrar la fecha actual en el campo de Fecha de Alta
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tvFechaAlta.text = getString(R.string.fecha_alta_label, currentDate)


        // Configuración del Spinner para el tipo de usuario
        val tiposDeUsuario = arrayOf("Empresa", "Autónomo", "Personal")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDeUsuario)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoUsuario.adapter = spinnerAdapter



        //sms lancher
        smsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // acá capturás el resultado del SMS si querés
                Toast.makeText(this, "SMS enviado correctamente", Toast.LENGTH_SHORT).show()
            }
        }





        // Botón GUARDAR
        btnGuardar.setOnClickListener {
            guardarUsuario()
        }

        // Botón CANCELAR
        btnCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)  // Opcionalmente enviar que se canceló
            finish()  // Solo cerrar esta pantalla
        }
    }

    private fun guardarUsuario() {
        // Recoger valores
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val tipoUsuario = spinnerTipoUsuario.selectedItem.toString()
        val nombreEmpresa = etNombreEmpresa.text.toString().trim()
        val estado = "Activo"
        val fechaBaja: String? = null

        // Validaciones
        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre completo es obligatorio.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidPhoneNumber(telefono)) {
            Toast.makeText(this, "El teléfono debe tener 9 cifras.", Toast.LENGTH_SHORT).show()
            return
        }
        if (nombreEmpresa.isEmpty() && tipoUsuario == "Empresa") {
            Toast.makeText(this, "El nombre de empresa es obligatorio para Empresas.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear usuario   ,no es un valor real para guardar, sino un placeholder temporal me refiero el ciero de id
        val usuario = Usuario(
            id_usuario = 0,
            nombre_completo = nombre,
            tipo_usuario = tipoUsuario,
            nombre_empresa_o_autonomo = nombreEmpresa,
            telefono = telefono,
            fecha_alta = currentDate,
            fecha_baja = fechaBaja,
            estado = estado
        )

        // Llamada a la API para insertar
        // Insertar usuario en la base de datos a través de la API
        RetrofitInstance.api.insertarUsuario(usuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@InsertarUsuarioActivity, "✅ Usuario guardado exitosamente.", Toast.LENGTH_SHORT).show()

                    // ✅ Devolver el usuario insertado a ListaUsuariosActivity
                    val intent = Intent()
                    intent.putExtra("usuarioInsertado", usuario)
                    setResult(RESULT_OK, intent)
                    finish()


                   // enviarSMS(usuario) // Después de guardar correctamente, enviamos el SMS
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@InsertarUsuarioActivity, "❌ Error ${response.code()}: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@InsertarUsuarioActivity, "❌ Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Capturamos el resultado de la actividad de SMS.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SMS && resultCode == RESULT_OK) {
            // Si el SMS se envió correctamente, volvemos a la lista
            setResult(RESULT_OK)
            finish()
        }
    }


    private fun isValidPhoneNumber(phone: String): Boolean {
        val pattern = Pattern.compile("^\\d{9}$")
        return pattern.matcher(phone).matches()
    }
}














