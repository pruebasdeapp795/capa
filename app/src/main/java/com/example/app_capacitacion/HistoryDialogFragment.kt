package com.example.app_capacitacion

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Capacitante

class HistoryDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_HISTORY_LIST = "history_list"

        fun newInstance(historyList: List<Capacitante>): HistoryDialogFragment {
            val fragment = HistoryDialogFragment()
            val args = Bundle()
            args.putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_history, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)

        val historyList = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<Capacitante> ?: emptyList()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = HistoryAdapter(historyList) { capacitante ->
            // Al hacer click en un item, mostrar detalles con DialogInfo
            val dialogInfo = DialogInfo.newInstance(capacitante)
            dialogInfo.show(parentFragmentManager, "DialogInfoTag")
            dismiss() // Cierra el diálogo de historial si quieres
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}
