package com.transdoor.app.ui.control

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.transdoor.app.R
import com.transdoor.app.model.Usuario


//creando un adaptador para tu RecyclerView que muestra la lista de usuarios.

class UsuarioAdapter(
    private val context: Context,
    private var listaUsuarios: List<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {


    // Creamos un listener para los clics en los ítems
    private var onItemClickListener: ((Usuario) -> Unit)? = null

    // Método para configurar el listener
    fun setOnItemClickListener(listener: (Usuario) -> Unit) {
        onItemClickListener = listener
    }

   // el metodo para actualizar la lista
   @SuppressLint("NotifyDataSetChanged")
   fun actualizarLista(nuevaLista: List<Usuario>) {
     listaUsuarios = nuevaLista
     notifyDataSetChanged()
 }

    // ViewHolder con referencias a los componentes visualesse encarga de almacenar las vistas (como el nombre, teléfono y tipo de usuario) dentro de cada ítem del RecyclerView
    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val cardUsuario: CardView = itemView.findViewById(R.id.cardUsuario)
        init {
            // Manejamos el clic sobre cada tarjeta
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val usuario = listaUsuarios[position]
                    onItemClickListener?.invoke(usuario)
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun getItemCount(): Int = listaUsuarios.size




//Son métodos que se encargan de inflar las vistas y vincular los datos de cada usuario a las vistas correspondientes
    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]

        // Setear valores en la vista
        holder.tvNombre.text = usuario.nombre_completo
        holder.tvTelefono.text = usuario.telefono

    // Mostrar tipo como letra: P, E, A
    holder.tvTipo.text = when (usuario.tipo_usuario.lowercase()) {
        "personal" -> "P"
        "empresa" -> "E"
        "autónomo", "autonomo" -> "A"
        else -> "?"
    }

        // Cambiar color según el estado
        val color = if (usuario.estado == "Activo") {
            ContextCompat.getColor(context, R.color.verde_suave)
        } else {
            ContextCompat.getColor(context, R.color.naranja_suave)
        }
        holder.cardUsuario.setCardBackgroundColor(color)
    }




}
