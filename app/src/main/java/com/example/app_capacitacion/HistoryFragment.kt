package com.example.app_capacitacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Capacitante

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var noHistoryTextView: TextView

    companion object {
        private const val ARG_HISTORY_LIST = "history_list"

        fun newInstance(historyList: List<Capacitante>): HistoryFragment {
            val fragment = HistoryFragment()
            val args = Bundle().apply {
                // Como Capacitante es Serializable, puedes ponerlo directamente
                putSerializable(ARG_HISTORY_LIST, ArrayList(historyList))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.historyRecyclerView)
        noHistoryTextView = view.findViewById(R.id.noHistoryTextView)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Recupera la lista del historial de los argumentos
        val scanHistory = arguments?.getSerializable(ARG_HISTORY_LIST) as? List<Capacitante> ?: emptyList()

        if (scanHistory.isEmpty()) {
            noHistoryTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noHistoryTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            // THIS IS THE KEY CHANGE: Pass the click listener to your adapter
            historyAdapter = HistoryAdapter(scanHistory) { clickedCapacitante ->
                // When an item in the history is clicked, show the DialogInfo
                val dialogInfo = DialogInfo.newInstance(clickedCapacitante)
                // Use parentFragmentManager to show the dialog over the whole activity
                dialogInfo.show(parentFragmentManager, "DialogInfoTag")
            }
            recyclerView.adapter = historyAdapter
        }

        return view
    }

}