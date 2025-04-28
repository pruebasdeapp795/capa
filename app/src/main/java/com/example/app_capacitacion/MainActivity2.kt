package com.example.app_capacitacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            enableEdgeToEdge()
        }
        setContentView(R.layout.activity_main2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editText: EditText = findViewById(R.id.esit_text)
        val button: Button = findViewById(R.id.buttonScan)
        val qrImageView: ImageView = findViewById(R.id.qr_image)


        button.setOnClickListener {
            val multiFormatWriter = MultiFormatWriter()

            try {

                val bitMatrix = multiFormatWriter.encode(
                    editText.text.toString(),
                    com.google.zxing.BarcodeFormat.QR_CODE,
                    300,
                    300
                )

                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)


                qrImageView.setImageBitmap(bitmap)


                showToast("QR generado correctamente")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error al generar el QR.")
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
