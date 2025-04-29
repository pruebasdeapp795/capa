package com.example.app_capacitacion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import android.app.AlertDialog
import android.content.Context
import androidx.activity.OnBackPressedCallback
import retrofit2.Call
import retrofit2.Response


class HomeActivity : AppCompatActivity(), ConfirmLogoutDialogFragment.OnLogoutConfirmationListener  {

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScan()
            } else {
                showToast("Se necesita permiso de cámara para escanear QR.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                }
                R.id.navigation_dashboard -> {
                    Toast.makeText(this, "Panel seleccionado", Toast.LENGTH_SHORT).show()

                }
                R.id.navigation_notifications -> {
                    mostrarDialogoConfirmacion()
                }
            }
            true
        }

        val scanButton: Button = findViewById(R.id.scan_button)
        scanButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startScan()
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle("¿Desea salir?")
                    .setMessage("¿Está seguro que desea salir de la aplicación?")
                    .setPositiveButton("Sí") { dialog, _ ->
                        dialog.dismiss()
                        finishAffinity()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanear QR")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                showToast("Escaneo cancelado")
            } else {
                val scannedData = result.contents
                if (isValidContent(scannedData)) {
                    showToast("Código escaneado: $scannedData")
                    if (isValidUrl(scannedData)) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scannedData))
                        startActivity(intent)
                    }
                } else {
                    showToast("El QR escaneado no es válido. ")
                }
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            Uri.parse(url).scheme?.startsWith("http") == true
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidContent(content: String): Boolean {
        return content.matches("\\d+".toRegex()) || isValidUrl(content)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLogoutConfirmed() {
        performLogout()
    }

    override fun onLogoutCancelled() {
        Toast.makeText(this, "Cierre de sesión cancelado", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoConfirmacion() {
        val fm = supportFragmentManager
        val confirmLogoutDialogFragment = ConfirmLogoutDialogFragment()
        confirmLogoutDialogFragment.show(fm, "fragment_confirm_logout")
    }

    private fun performLogout() {
        val authToken = ApiClient.authToken

        if (!authToken.isNullOrBlank()) {
            ApiClient.apiService.logout("Bearer $authToken")
                .enqueue(object : retrofit2.Callback<com.example.app_capacitacion.Models.GeneralResponse> {
                    override fun onResponse(
                        call: Call<com.example.app_capacitacion.Models.GeneralResponse>,
                        response: Response<com.example.app_capacitacion.Models.GeneralResponse>
                    ) {
                        if (response.isSuccessful) {
                            ApiClient.setAuthToken(null)
                            val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                            sharedPref.edit().remove("access_token").apply()

                            Toast.makeText(
                                this@HomeActivity,
                                response.body()?.message ?: "Logout exitoso",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@HomeActivity,
                                "Error al cerrar sesión: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<com.example.app_capacitacion.Models.GeneralResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            this@HomeActivity,
                            "Error de conexión al cerrar sesión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this@HomeActivity, "No hay sesión activa para cerrar.", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
