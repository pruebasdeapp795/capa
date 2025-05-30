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

    // ¡NUEVO MÉTODO!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el estilo del diálogo aquí
        // Usa `R.style.FullScreenDialog` si tu archivo se llama `styles.xml`
        // O `R.style.Theme_TuApp_FullScreenDialog` si lo definiste en `themes.xml` dentro de un Theme.
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog) // Asegúrate de que "FullScreenDialog" coincida con el nombre que le diste a tu estilo
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
            capacitante?.cursos?.let { coursesList ->
                val cursosDialogFragment = CursosDialogFragment.newInstance(coursesList)
                cursosDialogFragment.show(childFragmentManager, CursosDialogFragment.TAG)
            } ?: run {
                // Maneja el caso en el que no haya cursos
                Toast.makeText(context, "No hay cursos disponibles para este capacitante.", Toast.LENGTH_SHORT).show()
            }
        }

        val mostrarInfoAdicionalButton: Button = view.findViewById(R.id.boton_info_addi)
        mostrarInfoAdicionalButton.setOnClickListener {
            val fechaIngreso = "2025-04-21"
            val cargo = "Ejemplo de cargo"
            val empresa = "Ejemplo de empresa"
            val nit = capacitante?.nit ?: "N/A"

            val additionalInfoBottomSheet = AdditionalInfoBottomSheet.newInstance(fechaIngreso, cargo, empresa, nit)
            additionalInfoBottomSheet.show(childFragmentManager, AdditionalInfoBottomSheet.TAG)
        }
    }
}