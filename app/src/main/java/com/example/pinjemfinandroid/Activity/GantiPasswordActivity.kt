package com.example.pinjemfinandroid.Activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.Utils.combineLoading
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityDashboardBinding
import com.example.pinjemfinandroid.databinding.ActivityGantiPasswordBinding

class GantiPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGantiPasswordBinding
    private lateinit var preferenceHelper: PreferenceHelper
    private val authViewModel:AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantiPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceHelper = PreferenceHelper(this)
        isLoading()
        animation()
        binding.buttonsignup.setOnClickListener {
            if (binding.etNewPassword.text.toString()== binding.etConfirmPassword.text.toString()){
                preferenceHelper.getString("token")?.let { it1 ->
                    authViewModel.updatePassword(
                        it1,binding.etOldpassword.text.toString(),binding.etNewPassword.text.toString())
                }
            }
            else
            {
                Toast.makeText(this,"Password dan kongirmasi password berbeda",Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.updatePasswordResult.observe(this){
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,DashboardActivity::class.java))
        }
        authViewModel.updatePasswordError.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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



    private fun animation(){
        val myAnimation = Animation()
        myAnimation.animationslidebottom(binding.linearLayout)
        myAnimation.animationslideleft(binding.vHand)
        myAnimation.animationslideright(binding.vCircle)
        myAnimation.animationslideright(binding.vCreateAccount)

    }
}