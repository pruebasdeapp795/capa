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
import com.example.app_capacitacion.Models.MessageResponse.ResetPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var tokenEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        tokenEditText = findViewById(R.id.txtcode)
        passwordEditText = findViewById(R.id.txtpassword)
        confirmPasswordEditText = findViewById(R.id.txtnewpassword)

        resetButton.setOnClickListener {
            val  token = tokenEditText.text.toString().trim()
            val  password = passwordEditText.text.toString().trim()
            val  confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (token.isNotEmpty() && password.isNotEmpty() && password == confirmPassword){
                resetPassword(token, password, confirmPassword)
            }else{
                Toast.makeText(this, "Por favor, completa todos los campos correctamente ", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun resetPassword(token: String, password:String, passwordConfirmation: String){

        ApiClient.apiService.resetPassword(token, password, passwordConfirmation)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                    if (response.isSuccessful) {
                        val resetPasswordResponse = response.body()
                        Log.d("ResetPassword", "Éxito: ${resetPasswordResponse?.message}")
                        Toast.makeText(this@ResetPasswordActivity, resetPasswordResponse?.message ?: "Contraseña restablecida", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ResetPassword", "Error: ${response.code()} - $errorBody")
                        Toast.makeText(this@ResetPasswordActivity, "Error al restablecer la contraseña", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Log.e("ResetPassword", "Fallo en la conexión: ${t.message}")
                    Toast.makeText(this@ResetPasswordActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                }
            })
    }

}