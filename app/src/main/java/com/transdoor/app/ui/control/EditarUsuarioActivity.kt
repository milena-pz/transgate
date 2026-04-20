package com.transdoor.app.ui.control

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.transdoor.app.R
import com.transdoor.app.model.Usuario
import com.transdoor.app.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.Locale

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private lateinit var spinnerTipoUsuario: Spinner
    private lateinit var etNombreEmpresa: EditText
    private lateinit var tvFechaAlta: TextView
    private lateinit var tvFechaBaja: TextView
    private lateinit var tvEstado: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var checkSinFechaBaja: CheckBox
    private lateinit var usuario: Usuario
    private lateinit var fechaAltaSeleccionada: String
    private var fechaBajaSeleccionada: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        // 🧩 Enlazar vistas
        etNombre = findViewById(R.id.etNombreEditar)
        etTelefono = findViewById(R.id.etTelefonoEditar)
        spinnerTipoUsuario = findViewById(R.id.spinnerTipoUsuarioEditar)
        etNombreEmpresa = findViewById(R.id.etNombreEmpresaEditar)
        tvFechaAlta = findViewById(R.id.tvFechaAltaEditar)
        tvFechaBaja = findViewById(R.id.tvFechaBajaEditar)
        tvEstado = findViewById(R.id.tvEstadoEditar)
        btnGuardar = findViewById(R.id.btnGuardarEditar)
        btnCancelar = findViewById(R.id.btnCancelarEditar)
        checkSinFechaBaja = findViewById(R.id.checkSinFechaBaja)

        // Recibimos el usuario enviado desde la lista

       @Suppress("DEPRECATION")
        usuario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("usuario", Usuario::class.java)
        } else {
            intent.getSerializableExtra("usuario") as? Usuario
        } ?: run {
            Toast.makeText(this, "Error al recibir usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }






        // Configurar spinner
        val tiposUsuario = arrayOf("Empresa", "Autónomo", "Personal")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposUsuario)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoUsuario.adapter = spinnerAdapter

        // Cargar datos del usuario en los campos
        cargarDatosUsuario()

        // Picker fecha alta
        tvFechaAlta.setOnClickListener {
            mostrarDatePicker { fechaSeleccionada ->
                fechaAltaSeleccionada = fechaSeleccionada
                tvFechaAlta.text = getString(R.string.fecha_de_altaEditar, fechaAltaSeleccionada)
            }
        }

        // Picker fecha baja
        tvFechaBaja.setOnClickListener {
            // Si está marcado "Sin fecha", no permitimos seleccionar fecha
            if (checkSinFechaBaja.isChecked) {
                Toast.makeText(this, "Está activado 'Sin fecha de baja'", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mostrarDatePicker { fechaSeleccionada ->
                fechaBajaSeleccionada = fechaSeleccionada
                tvFechaBaja.text = getString(R.string.fecha_de_bajaEditar, fechaBajaSeleccionada)
                tvEstado.text = getString(R.string.estado_bajaEditar)
            }
        }

        // Lógica del checkbox "Sin fecha de baja"
        checkSinFechaBaja.setOnCheckedChangeListener { _, isChecked ->
            // Cambiar apariencia del campo fecha
            tvFechaBaja.isEnabled = !isChecked
            tvFechaBaja.setTextColor(if (isChecked) getColor(android.R.color.darker_gray) else getColor(android.R.color.black))

            if (isChecked) {
                // Si está marcado: desactiva, limpia y pone estado "Activo"
                fechaBajaSeleccionada = null
                tvFechaBaja.text = getString(R.string.fecha_de_bajaEditar, "—")
                tvEstado.text = getString(R.string.estadoEditar, "Activo")
            } else {
                // Si se desmarca: recupera fecha anterior (si hay)
                if (!usuario.fecha_baja.isNullOrEmpty()) {
                    fechaBajaSeleccionada = usuario.fecha_baja
                    tvFechaBaja.text = getString(R.string.fecha_de_bajaEditar, usuario.fecha_baja)
                    tvEstado.text = getString(R.string.estadoEditar, usuario.estado)
                }
            }
        }





        // Cancelar
        btnCancelar.setOnClickListener { finish() }

        // Guardar
        btnGuardar.setOnClickListener { guardarCambios() }
    }
    @Suppress("UNCHECKED_CAST")
    private fun cargarDatosUsuario() {
        etNombre.setText(usuario.nombre_completo)
        etTelefono.setText(usuario.telefono)
        etNombreEmpresa.setText(usuario.nombre_empresa_o_autonomo)

        val index = (spinnerTipoUsuario.adapter as ArrayAdapter<String>)
            .getPosition(usuario.tipo_usuario)
        spinnerTipoUsuario.setSelection(index)

        fechaAltaSeleccionada = usuario.fecha_alta
        fechaBajaSeleccionada = usuario.fecha_baja

        tvFechaAlta.text = getString(R.string.fecha_de_altaE, usuario.fecha_alta)
        tvFechaBaja.text = if (usuario.fecha_baja != null && usuario.fecha_baja!!.isNotEmpty())
            "Fecha de Baja: ${usuario.fecha_baja}"
        else "Fecha de Baja: —"

        tvEstado.text = getString(R.string.estadoEditar, usuario.estado)
    }

    private fun mostrarDatePicker(onFechaSeleccionada: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onFechaSeleccionada(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun guardarCambios() {
        // Validaciones
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val nombreEmpresa = etNombreEmpresa.text.toString().trim()
        val tipoUsuario = spinnerTipoUsuario.selectedItem.toString()

        if (nombre.isEmpty() || telefono.isEmpty() || nombreEmpresa.isEmpty()) {
            Toast.makeText(this, "❌ Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!telefono.matches(Regex("^\\d{9}$"))) {
            Toast.makeText(this, "❌ Teléfono inválido (9 dígitos)", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear usuario actualizado
        val usuarioEditado = Usuario(
            id_usuario = usuario.id_usuario,
            nombre_completo = nombre,
            telefono = telefono,
            tipo_usuario = tipoUsuario,
            nombre_empresa_o_autonomo = nombreEmpresa,
            fecha_alta = fechaAltaSeleccionada,
            fecha_baja = fechaBajaSeleccionada,
            estado = if (fechaBajaSeleccionada != null && fechaBajaSeleccionada!!.isNotEmpty()) "Baja" else "Activo"
        )

        Log.d("UsuarioEditado", usuarioEditado.toString())

        // Enviar por Retrofit
        RetrofitInstance.api.editarUsuario(usuarioEditado).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("EDITAR_USUARIO", "✅ Usuario editado con éxito")
                    Toast.makeText(this@EditarUsuarioActivity, "Usuario editado correctamente", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Log.e("EDITAR_USUARIO", "❌ Error HTTP: ${response.code()}")
                    Toast.makeText(this@EditarUsuarioActivity, "Error HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

                checkSinFechaBaja.isChecked = usuario.fecha_baja.isNullOrEmpty()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("EDITAR_USUARIO", "❌ Error de red: ${t.message}")
                Toast.makeText(this@EditarUsuarioActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })





    }
}
