package com.transdoor.app.chat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.transdoor.app.R


class ChatAdapter(private val mensajes: List<Mensaje>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].esUsuario) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == 1)
            R.layout.item_mensaje_usuario
        else
            R.layout.item_mensaje_bot

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MensajeViewHolder).bind(mensajes[position])
    }

    override fun getItemCount(): Int = mensajes.size

    class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtMensaje: TextView = itemView.findViewById(R.id.txtMensaje)

        fun bind(mensaje: Mensaje) {
            txtMensaje.text = mensaje.texto
        }
    }
}

