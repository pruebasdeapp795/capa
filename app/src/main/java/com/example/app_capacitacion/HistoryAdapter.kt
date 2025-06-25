package com.example.app_capacitacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Capacitante

class HistoryAdapter( private val historyList: List<Capacitante>,
    // This is the crucial change: add a clickListener parameter
                      private val clickListener: (Capacitante) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCapacitanteName)
        val tvDocument: TextView = itemView.findViewById(R.id.tvCapacitanteDocument)
        val tvCompany: TextView = itemView.findViewById(R.id.tvCapacitanteCompany)

        // Agrega más TextViews si quieres mostrar más datos del capacitante
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val capacitante = historyList[position]
        holder.tvName.text = "Nombre: ${capacitante.nombre}"
        holder.tvDocument.text = "Documento: ${capacitante.numero_documento}"
        holder.tvCompany.text = "Empresa: ${capacitante.empresa}"
        // Asigna más datos aquí

        // This is the second crucial change: set the click listener on the item
        holder.itemView.setOnClickListener {
            clickListener(capacitante) // Call the passed-in lambda with the current Capacitante
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}