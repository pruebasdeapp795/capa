package com.example.app_capacitacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.app_capacitacion.databinding.HomeMainBinding
import retrofit2.Call
import retrofit2.Response

class HomeActivity : AppCompatActivity(),ConfirmLogoutDialogFragment.OnLogoutConfirmationListener  {


    private  lateinit var  binding: HomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeCapa())

        val userName = intent.getStringExtra("USER_NAME")
        val greetingTextView: TextView = findViewById(R.id.textView)
        val initialsTextView: TextView = findViewById(R.id.user_initials)

        if (!userName.isNullOrEmpty()) {
            greetingTextView.text = "Hola, $userName"

            val nameParts = userName.split(" ")
            val initials = StringBuilder()
            var count = 0

            for (part in nameParts) {
                if (part.isNotBlank() && count < 2) {
                    initials.append(part[0].uppercaseChar())
                    count++
                }
                if (count >= 2) {
                    break
                }
            }
            initialsTextView.text = initials.toString()
        } else {
            greetingTextView.text = "Hola, usuario"
            initialsTextView.text = "U"
        }


        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.navigation_info -> replaceFragment(DialogInfo())
                R.id.navigation_dashboard -> replaceFragment(HomeCapa())
                R.id.navigation_notifications -> {
                    mostrarDialogoConfirmacion()
                    true
                }
                else -> {

                }

            }
            true


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

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout,fragment)
        fragmentTransition.commit()

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


