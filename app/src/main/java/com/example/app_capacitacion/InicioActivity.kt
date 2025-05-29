package com.example.app_capacitacion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InicioActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inicio_main4)

        val logbutton: Button = findViewById(R.id.consul)

        logbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@InicioActivity)
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
}