package com.example.pinjemfinandroid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityCreatePasswordAuthGoogleBinding
import com.example.pinjemfinandroid.databinding.ActivityDashboardBinding


class CreatePasswordAuthGoogleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePasswordAuthGoogleBinding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordAuthGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animation()

        // Observasi hasil pendaftaran
        authViewModel.registerAuthGoogleResult.observe(this) { tokenResponse ->
            tokenResponse?.let {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
            }

            binding
        }

        authViewModel.registerAuthGoogleError.observe(this) { errorMessage ->
            // Ketika gagal
            errorMessage?.let {
                Toast.makeText(this, "Registration Failed: $it", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonsignup.setOnClickListener {
            if (binding.etPassword.text.toString().equals(binding.etConfirmPassword.text.toString())){
                intent.getStringExtra("firebaseEmail")?.let { it1 ->
                    authViewModel.PostRegisterUserAuthGoogle(
                        it1,binding.etPassword.text.toString(), intent.getStringExtra("firebaseUsername")!!
                    )
                }
            }
            else{
                Toast.makeText(this, "password and confirm password are different", Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun animation(){
        val myAnimation = Animation()
        myAnimation.animationslidebottom(binding.linearLayout)
        myAnimation.animationslideright(binding.vCreateAccount)
        myAnimation.animationslideleft(binding.vHand)
        myAnimation.animationPopup(binding.vCircle)
    }
}