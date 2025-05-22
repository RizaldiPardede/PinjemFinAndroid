package com.example.pinjemfinandroid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.combineLoading
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityForgotPasswordBinding
import com.example.pinjemfinandroid.databinding.ActivityGantiPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val authViewModel:AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isLoading()
        binding.etEmail.setOnTouchListener{ v, event ->
            if (event.action == MotionEvent.ACTION_UP) {


                val drawableEnd = binding.etEmail.compoundDrawables[2] // 0=left, 1=top, 2=right, 3=bottom
                if (drawableEnd != null) {
                    val iconStart = binding.etEmail.width - binding.etEmail.paddingEnd - drawableEnd.intrinsicWidth
                    if (event.x >= iconStart) {
                        // Icon diklik → lakukan aksi (misal toggle visibility)
                        authViewModel.forgotPassword(binding.etEmail.text.toString())
                        return@setOnTouchListener true
                    }
                }

            }
            false
        }

        authViewModel.forgotPasswordResult.observe(this){
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
        }

        authViewModel.forgotPasswordError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }




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