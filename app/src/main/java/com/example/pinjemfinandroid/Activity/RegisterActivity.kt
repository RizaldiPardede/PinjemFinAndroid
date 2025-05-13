package com.example.pinjemfinandroid.Activity


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonsignup.backgroundTintList= null
        animation()
        // Observasi hasil pendaftaran
        authViewModel.registerResult.observe(this, { tokenResponse ->
            // Ketika berhasil
            tokenResponse?.let {
                Toast.makeText(this, "Registration Successful! Token: ${it.token}", Toast.LENGTH_SHORT).show()
            }
        })

        authViewModel.registerError.observe(this, { errorMessage ->
            // Ketika gagal
            errorMessage?.let {
                Toast.makeText(this, "Registration Failed: $it", Toast.LENGTH_SHORT).show()
            }
        })
        binding.buttonsignup.setOnClickListener {
            if (binding.etPassword.text.toString().equals(binding.etConfirmPassword.text.toString())){
                register(binding.etEmail.text.toString(),binding.etPassword.text.toString(),binding.etUsername.text.toString())
            }
            else{
                Toast.makeText(this, "password and confirm password are different", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun register(username: String, password: String, nama: String){

        authViewModel.PostRegisterUser(username, password, nama)

    }

    private fun animation(){
        val myAnimation = Animation()
        myAnimation.animationslidebottom(binding.linearLayout)
        myAnimation.animationslideright(binding.vCreateAccount)
        myAnimation.animationslideleft(binding.vHand)
        myAnimation.animationPopup(binding.vCircle)
    }
}