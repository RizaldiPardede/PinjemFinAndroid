package com.example.pinjemfinandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonloggin.backgroundTintList = null
        binding.buttonsignup.backgroundTintList = null
        animation()
        preferenceHelper = PreferenceHelper(this)


        binding.buttonsignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonloggin.setOnClickListener {
            authViewModel.PostLoginUser(binding.etEmail.text.toString(),binding.etPassword.text.toString())
            // Observasi hasil pendaftaran
            authViewModel.loginResult.observe(this, { tokenResponse ->
                // Ketika berhasil
                tokenResponse?.let {

                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    it.token?.let { it1 -> preferenceHelper.setString("token", it1) }

                    startActivity(Intent(this, DashboardActivity::class.java))
                }
            })

            authViewModel.loginError.observe(this, { errorMessage ->
                // Ketika gagal
                errorMessage?.let {
                    Toast.makeText(this, "Email atau Password Salah", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun animation(){
        val myAnimation = Animation()
        myAnimation.animationslideright(binding.textView)
        myAnimation.animationslideright(binding.textView2)
        myAnimation.animationslidebottom(binding.linearLayout)
        myAnimation.animationslideleft(binding.imageView2)
        myAnimation.animationslideleft(binding.circleLine)
        myAnimation.animationPopup(binding.circle)

    }


}