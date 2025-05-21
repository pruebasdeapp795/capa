package com.example.app_capacitacion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.app_capacitacion.Models.LoginRequest
import com.example.app_capacitacion.Models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import android.widget.Toast
import androidx.activity.OnBackPressedCallback


class LoginActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val loginOlvidaste: Button = findViewById(R.id.olvidaste)
        val loginBack: Button = findViewById(R.id.buttonback)
        val loginButton: Button = findViewById(R.id.loginButton)
        val checkRecordarme: CheckBox = findViewById(R.id.radioButton)

        val emailEditText = findViewById<EditText>(R.id.user)
        val passwordEditText = findViewById<EditText>(R.id.password)

        val savedEmail = getUserEmail()
        if (savedEmail != null) {
            emailEditText.setText(savedEmail)
            checkRecordarme.isChecked = true
        }

        loginOlvidaste.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBack.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@LoginActivity)
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

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val recordar = checkRecordarme.isChecked

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)

            ApiClient.apiService.login(loginRequest).enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            ApiClient.setAuthToken(it.access_token)

                            if (recordar) {
                                saveUserEmail(email)
                            } else {
                                clearUserEmail()
                            }

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("USER_NAME", it.user.name)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val errorMessage = when (response.code()) {
                            401 -> "Credenciales incorrectas"
                            else -> "Error de inicio de sesión: ${response.code()}"
                        }
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val errorMessage = "Error de conexión: ${t.message}"
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun saveUserEmail(email: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().putString("user_email", email).apply()
    }

    fun getUserEmail(): String? {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getString("user_email", null)
    }

    fun clearUserEmail() {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().remove("user_email").apply()
    }
}