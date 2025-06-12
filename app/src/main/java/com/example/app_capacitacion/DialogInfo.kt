package com.example.app_capacitacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.app_capacitacion.Models.Capacitante

class DialogInfo : DialogFragment() {

    companion object {
        private const val ARG_CAPACITANTE = "capacitante_object"
        fun newInstance(capacitante: Capacitante): DialogInfo {
            val fragment = DialogInfo()
            val args = Bundle().apply {
                putSerializable(ARG_CAPACITANTE, capacitante)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
        arguments?.let {
            capacitante = it.getSerializable(ARG_CAPACITANTE) as? Capacitante
        }
    }

    private var capacitante: Capacitante? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val capacitante = arguments?.getSerializable(ARG_CAPACITANTE) as? Capacitante

        capacitante?.let {
            view.findViewById<TextView>(R.id.nombre_persona).text = it.nombre
            view.findViewById<TextView>(R.id.tipo_documento_persona).text = it.tipo_documento
            view.findViewById<TextView>(R.id.cedula_persona).text = it.numero_documento
            view.findViewById<TextView>(R.id.Telefono_persona).text = it.telefono
            view.findViewById<TextView>(R.id.correo_persona).text = it.correo
            view.findViewById<TextView>(R.id.estado_persona).text = it.estado
            view.findViewById<TextView>(R.id.nit_persona).text = it.nit

        }

        val mostrarCursosButton: Button = view.findViewById(R.id.boton_cursos)
        mostrarCursosButton.setOnClickListener {
            val cursos = capacitante?.cursos
            if (cursos.isNullOrEmpty()) {
                Toast.makeText(context, "No hay cursos disponibles para este capacitante.", Toast.LENGTH_SHORT).show()
            } else {
                val cursosDialogFragment = CursosDialogFragment.newInstance(cursos)
                cursosDialogFragment.show(childFragmentManager, CursosDialogFragment.TAG)
            }
        }

        val mostrarInfoAdicionalButton: Button = view.findViewById(R.id.boton_info_addi)
        mostrarInfoAdicionalButton.setOnClickListener {
            val fechaIngreso = capacitante?.fecha_ingreso
            val cargo = capacitante?.cargo
            val empresa = capacitante?.empresa
            val nit = capacitante?.nit

            if (fechaIngreso != null && cargo != null && empresa != null && nit != null) {
                val additionalInfoBottomSheet = AdditionalInfoBottomSheet.newInstance(fechaIngreso, cargo, empresa, nit)
                additionalInfoBottomSheet.show(childFragmentManager, AdditionalInfoBottomSheet.TAG)
            } else {
                Toast.makeText(context, "Información adicional incompleta para este capacitante.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}