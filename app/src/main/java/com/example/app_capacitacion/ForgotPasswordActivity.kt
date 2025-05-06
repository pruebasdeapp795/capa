package com.example.app_capacitacion

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_capacitacion.Models.MessageResponse.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var sendRequestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        emailEditText = findViewById(R.id.textemail)
        sendRequestButton = findViewById(R.id.enviar)

        sendRequestButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isNotEmpty()){
                forgotPassword(email)
            }else{
                Toast.makeText(this, "Porfavor, ingresa tu correo electronica", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun forgotPassword(email: String) {
        ApiClient.apiService.forgotPassword(email).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful) {
                    val forgotPasswordResponse = response.body()
                    Log.d("ForgotPassword", "Éxito: ${forgotPasswordResponse?.message}")
                    Toast.makeText(this@ForgotPasswordActivity, forgotPasswordResponse?.message ?: "Solicitud enviada", Toast.LENGTH_LONG).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ForgotPassword", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@ForgotPasswordActivity, "Error al enviar la solicitud", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Log.e("ForgotPassword", "Fallo en la conexión: ${t.message}")
                Toast.makeText(this@ForgotPasswordActivity, "Error de conexión", Toast.LENGTH_LONG).show()
            }
        })
    }


}


