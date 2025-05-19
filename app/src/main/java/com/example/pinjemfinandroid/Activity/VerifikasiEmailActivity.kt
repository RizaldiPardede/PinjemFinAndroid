package com.example.pinjemfinandroid.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityVerifikasiEmailBinding

class VerifikasiEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifikasiEmailBinding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifikasiEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: Uri? = intent?.data
        val token = data?.getQueryParameter("token")
        animation()
        if (token != null) {
            authViewModel.PostEmailActivation(token)

            authViewModel.emailActivationResult.observe(this){
                if (it.message.equals("Activation Email Success")){
                    binding.ivStatus.setImageResource(R.drawable.baseline_check_circle_24)
                    binding.tvMessage.text = it.message.toString()
                }
                else{
                    binding.ivStatus.setImageResource(R.drawable.baseline_disabled_by_default_24)
                    binding.tvMessage.text = it.message.toString()
                }
            }

            authViewModel.emailActivationError.observe(this){
                binding.ivStatus.setImageResource(R.drawable.baseline_disabled_by_default_24)
                binding.tvMessage.text = it.toString()
            }
        } else {
            binding.ivStatus.setImageResource(R.drawable.baseline_mark_email_read_24)
            binding.tvMessage.text = "Email verifikasi telah dikirim"
        }
    }

    private fun animation(){
        val myAnimation = Animation()
        myAnimation.animationPopup(binding.linearLayout2)
        myAnimation.animationslideright(binding.ivCirlce)
        myAnimation.animationslideleft(binding.ivCirlceline)

    }
}