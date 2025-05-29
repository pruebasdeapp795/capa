package com.example.app_capacitacion

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AdditionalInfoBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AdditionalInfoBottomSheet"
        private const val ARG_FECHA_INGRESO = "fecha_ingreso"
        private const val ARG_CARGO = "cargo"
        private const val ARG_EMPRESA = "empresa"
        private const val ARG_NIT = "nit"

        fun newInstance(fechaIngreso: String, cargo: String, empresa: String, nit: String): AdditionalInfoBottomSheet {
            val fragment = AdditionalInfoBottomSheet()
            val args = Bundle().apply {
                putString(ARG_FECHA_INGRESO, fechaIngreso)
                putString(ARG_CARGO, cargo)
                putString(ARG_EMPRESA, empresa)
                putString(ARG_NIT, nit)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_additional_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fechaIngreso = arguments?.getString(ARG_FECHA_INGRESO)
        val cargo = arguments?.getString(ARG_CARGO)
        val empresa = arguments?.getString(ARG_EMPRESA)
        val nit = arguments?.getString(ARG_NIT)

        view.findViewById<TextView>(R.id.fecha_ingreso_persona_bs).text = fechaIngreso
        view.findViewById<TextView>(R.id.cargo_persona_bs).text = cargo
        view.findViewById<TextView>(R.id.empresa_persona_bs).text = empresa
        view.findViewById<TextView>(R.id.nit_persona_bs).text = nit

        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            dismiss()
        }

    }


}