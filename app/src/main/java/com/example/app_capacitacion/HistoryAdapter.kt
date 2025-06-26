package com.example.app_capacitacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Capacitante

class HistoryAdapter(
    private val historyList: List<Capacitante>,
    private val clickListener: (Capacitante) -> Unit  // Lambda para manejar clicks
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // ViewHolder que contiene las vistas de cada item
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCapacitanteName)
        val tvDocument: TextView = itemView.findViewById(R.id.tvCapacitanteDocument)
        val tvCompany: TextView = itemView.findViewById(R.id.tvCapacitanteCompany)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)  // Usa tu layout de item
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val capacitante = historyList[position]

        // Asignar datos a las vistas
        holder.tvName.text = "Nombre: ${capacitante.nombre}"
        holder.tvDocument.text = "Documento: ${capacitante.numero_documento}"
        holder.tvCompany.text = "Empresa: ${capacitante.empresa}"

        // Configurar el click listener para el item completo
        holder.itemView.setOnClickListener {
            clickListener(capacitante)  // Llama al lambda con el item clickeado
        }
    }

    override fun getItemCount(): Int = historyList.size
}