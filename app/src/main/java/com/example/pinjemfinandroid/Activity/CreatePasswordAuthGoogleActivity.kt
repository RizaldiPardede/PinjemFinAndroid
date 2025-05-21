package com.example.pinjemfinandroid.Activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.Utils.combineLoading
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.ViewModel.DokumenViewModel
import com.example.pinjemfinandroid.databinding.ActivityCreatePasswordAuthGoogleBinding
import com.example.pinjemfinandroid.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class CreatePasswordAuthGoogleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePasswordAuthGoogleBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordAuthGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceHelper = PreferenceHelper(this)
        animation()
        isLoading()


        // Observasi hasil pendaftaran
        authViewModel.registerAuthGoogleResult.observe(this) { tokenResponse ->
            tokenResponse?.let {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

            }



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

    fun isLoading(){

        val isLoading = combineLoading(
            authViewModel.isLoading
        )

        isLoading.observe(this) { loading ->
            if (loading) {
                binding.loadingOverlay.visibility = View.VISIBLE
                binding.lottieView.playAnimation()
            } else {
                binding.lottieView.cancelAnimation()
                binding.loadingOverlay.visibility = View.GONE
            }
        }
    }

}