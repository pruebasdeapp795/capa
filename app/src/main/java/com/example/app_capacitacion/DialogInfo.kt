package com.example.app_capacitacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class DialogInfo : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mostrarCursosButton: Button = view.findViewById(R.id.boton_cursos)
        mostrarCursosButton.setOnClickListener {
            val cursosDialogFragment = CursosDialogFragment.newInstance()
            cursosDialogFragment.show(childFragmentManager, CursosDialogFragment.TAG)
        }
    }


}