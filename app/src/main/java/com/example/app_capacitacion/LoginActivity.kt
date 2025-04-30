package com.example.app_capacitacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_capacitacion.Models.LoginRequest
import com.example.app_capacitacion.Models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import android.widget.Toast


class LoginActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_main)

        toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val loginback: Button = findViewById(R.id.buttonback)

        val loginButton: Button = findViewById(R.id.loginButton)

        loginback.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.user).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            val loginRequest = LoginRequest(email, password)

            ApiClient.apiService.login(loginRequest).enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            ApiClient.setAuthToken(it.access_token)


                            val userName = it.user.name

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("USER_NAME", userName)
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
}