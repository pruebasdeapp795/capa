package com.example.app_capacitacion

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import android.Manifest

class HomeCapa : Fragment() {


    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScan()
            } else {
                showToast("Se necesita permiso de cámara para escanear QR.")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_capa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanButton: Button = view.findViewById(R.id.scan_button)
        scanButton.setOnClickListener {
            checkCameraPermissionAndStartScan()
        }

        val scanButton2: Button = view.findViewById(R.id.scan_button2)
        scanButton2.setOnClickListener {
            checkCameraPermissionAndStartScan()
        }
    }

    private fun checkCameraPermissionAndStartScan() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startScan()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startScan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanear QR")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                showToast("Escaneo cancelado")
            } else {
                val scannedContent = result.contents
                if (isValidContent(scannedContent)) {
                    showToast("Contenido escaneado: $scannedContent")
                    if (isValidUrl(scannedContent)) {
                        try {
                            val browserIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(scannedContent))
                            startActivity(browserIntent)
                        } catch (e: Exception) {
                            showToast("No se pudo abrir la URL.")
                        }
                    } else {
                    }
                } else {
                    showToast("Contenido QR no válido.")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            Uri.parse(url).scheme?.startsWith("http") == true || Uri.parse(url).scheme?.startsWith("https") == true
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidContent(content: String): Boolean {
        return content.matches("\\d+".toRegex()) || isValidUrl(content)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}