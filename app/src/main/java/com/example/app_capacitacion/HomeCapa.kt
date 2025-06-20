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
import com.example.app_capacitacion.Models.Capacitante
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val historyButton: Button = view.findViewById(R.id.history_button)
        val scanButton2: Button = view.findViewById(R.id.scan_button2)

        scanButton2.setOnClickListener { checkCameraPermissionAndStartScan() }

        historyButton.setOnClickListener {

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
                if (isValidUrl(scannedContent)) {
                    val documento = extractDocumentoFromUrl(scannedContent)
                    if (documento != null) {
                        fetchCapacitanteByDocumento(documento)
                    } else {
                        showToast("URL escaneada no es válida para capacitantes.")
                    }
                } else {
                    showToast("El contenido del QR no es una URL válida.")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun extractDocumentoFromUrl(url: String): String? {
        val uri = Uri.parse(url)
        val pathSegments = uri.pathSegments
        return if (pathSegments.size >= 2 && pathSegments[pathSegments.size - 2] == "capacitantes") {
            pathSegments.last()
        } else {
            null
        }
    }

    private fun fetchCapacitanteByDocumento(documento: String) {
        ApiClient.getApi().getCapacitante(documento).enqueue(object : Callback<Capacitante> {
            override fun onResponse(call: Call<Capacitante>, response: Response<Capacitante>) {
                if (response.isSuccessful) {
                    val capacitante = response.body()
                    if (capacitante != null) {
                        // ¡Aquí está el cambio! Usar .show() para un DialogFragment
                        val dialogInfoFragment = DialogInfo.newInstance(capacitante)
                        dialogInfoFragment.show(parentFragmentManager, "DialogInfoTag") // Puedes usar cualquier tag
                    } else {
                        showToast("Capacitante no encontrado o datos nulos.")
                    }
                } else {
                    showToast("Capacitante no encontrado.")
                }
            }

            override fun onFailure(call: Call<Capacitante>, t: Throwable) {
                showToast("Error de red: ${t.message}")
            }
        })
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val scheme = Uri.parse(url).scheme
            scheme?.startsWith("http") == true || scheme?.startsWith("https") == true
        } catch (e: Exception) {
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}