package com.example.app_capacitacion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class ConfirmLogoutDialogFragment : DialogFragment() {
    private var listener: OnLogoutConfirmationListener? = null

    interface OnLogoutConfirmationListener {
        fun onLogoutConfirmed()
        fun onLogoutCancelled()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnLogoutConfirmationListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnLogoutConfirmationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_logout, container, false)

        val buttonAccept = view.findViewById<Button>(R.id.buttonAccept)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        buttonAccept.setOnClickListener {
            listener?.onLogoutConfirmed()
            dismiss()
        }

        buttonCancel.setOnClickListener {
            listener?.onLogoutCancelled()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}