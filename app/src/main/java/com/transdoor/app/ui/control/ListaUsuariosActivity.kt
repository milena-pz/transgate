package com.transdoor.app.ui.control

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.transdoor.app.MainActivity
import com.transdoor.app.R
import com.transdoor.app.model.Usuario
import com.transdoor.app.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import kotlin.system.exitProcess

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerViewUsuarios: RecyclerView
    private lateinit var floatingActionButtonInsertar: FloatingActionButton
    private lateinit var floatingActionButtonLogout: FloatingActionButton
    private lateinit var floatingActionButtonSalir: FloatingActionButton
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var etFiltroNombre: EditText
    private lateinit var etFiltroTelefono: EditText
    // Lista completa (sin filtrar) y el adapter reutilizable
    private var listaCompletaUsuarios: List<Usuario> = emptyList()
    private lateinit var adapter: UsuarioAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        // Vinculamos vistas
        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios)
        floatingActionButtonInsertar = findViewById(R.id.floatingActionButtonInsertar)
        floatingActionButtonLogout = findViewById(R.id.floatingActionButtonLogout)
        floatingActionButtonSalir = findViewById(R.id.floatingActionButtonSalir)
        etFiltroNombre = findViewById(R.id.etFiltroNombre)
        etFiltroTelefono = findViewById(R.id.etFiltroTelefono)


        // Configuramos el RecyclerView con un adapter vacío
        adapter = UsuarioAdapter(this, emptyList())
        recyclerViewUsuarios.layoutManager = LinearLayoutManager(this)
        recyclerViewUsuarios.adapter = adapter


        // Listener de clics en usuarios,Configurar listener de clics una sola vez
        adapter.setOnItemClickListener { usuario ->
            val intent = Intent(this, VerUsuarioActivity::class.java)
            intent.putExtra("usuario", usuario as Serializable)
            resultLauncher.launch(intent)

        }


        // Escuchadores de filtros
        etFiltroNombre.addTextChangedListener(filtroWatcher)
        etFiltroTelefono.addTextChangedListener(filtroWatcher)


        // Llamamos a la API para obtener los usuarios
        listarUsuarios() // Cargar usuarios al iniciar



        // Configurar el ActivityResultLauncher para manejar el resultado de la actividad
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                listarUsuarios() // ✅ Se actualiza la lista con el nuevo usuario arriba

             //  val usuario = result.data?.getSerializableExtra("usuarioInsertado") as? Usuario
                @Suppress("DEPRECATION")
                val usuario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("usuarioInsertado", Usuario::class.java)
                } else {
                    result.data?.getSerializableExtra("usuarioInsertado") as? Usuario
                }



                usuario?.let {
                    // ✅ Ahora sí, lanzamos SMSAlta desde aquí
                    val smsIntent = Intent(this, SmsAltaActivity::class.java)
                    smsIntent.putExtra("usuario", it)
                    startActivity(smsIntent)
                }
            }
        }



        // Botón para insertar nuevo usuario
        floatingActionButtonInsertar.setOnClickListener {
            val intent = Intent(this, InsertarUsuarioActivity::class.java)
            resultLauncher.launch(intent)
        }

        // Botón para logout
        floatingActionButtonLogout.setOnClickListener {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botón para salir
        floatingActionButtonSalir.setOnClickListener {
            finishAffinity()
            exitProcess(0)
        }
    }

    // Filtro combinado por nombre y teléfono
    private fun aplicarFiltros() {
        val filtroNombre = etFiltroNombre.text.toString().trim().lowercase()
        val filtroTelefono = etFiltroTelefono.text.toString().trim()

        val listaFiltrada = listaCompletaUsuarios.filter { usuario ->
            val coincideNombre = usuario.nombre_completo.lowercase().contains(filtroNombre)
            val coincideTelefono = usuario.telefono.contains(filtroTelefono)
            coincideNombre && coincideTelefono
        }

        // Actualizamos la lista mostrada (sin crear nuevo adapter)
        adapter.actualizarLista(listaFiltrada)
    }

    // Watcher para detectar cambios en los campos de filtro
    private val filtroWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            aplicarFiltros()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    // Petición a la API para obtener la lista de usuarios
    private fun listarUsuarios() {
        RetrofitInstance.api.listarUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body() ?: emptyList()

                    if (usuarios.isEmpty()) {
                        Toast.makeText(this@ListaUsuariosActivity, "No hay usuarios disponibles", Toast.LENGTH_SHORT).show()
                        adapter.actualizarLista(emptyList())
                    } else {
                        // 🔥 Ordenamos por fecha de alta DESCENDENTE (más nuevo primero)
                        listaCompletaUsuarios = usuarios.sortedWith(
                            compareBy<Usuario> { it.estado != "Activo" } // Activos primero
                                .thenBy { it.nombre_completo.lowercase() } // Luego orden alfabético
                        )


                        adapter.actualizarLista(listaCompletaUsuarios)

                    }
                } else {
                    Toast.makeText(this@ListaUsuariosActivity, "Error al cargar los usuarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@ListaUsuariosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}







