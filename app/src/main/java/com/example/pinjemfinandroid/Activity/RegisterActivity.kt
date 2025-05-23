package com.example.pinjemfinandroid.Activity


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.Utils.combineLoading
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
        isLoading()
        clickablelogin()
        // Observasi hasil pendaftaran
        authViewModel.registerResult.observe(this) { tokenResponse ->
            tokenResponse?.let {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,VerifikasiEmailActivity::class.java))
            }
        }

        authViewModel.registerError.observe(this) { errorMessage ->
            // Ketika gagal
            errorMessage?.let {
                Toast.makeText(this, "Registration Failed: $it", Toast.LENGTH_SHORT).show()
            }
        }
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

    fun clickablelogin(){
        val fullText = "Sudah memiliki akun?  Login"
        val spannableString = SpannableString(fullText)

        val loginText = "Login"
        val startIndex = fullText.indexOf(loginText)
        val endIndex = startIndex + loginText.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.BLUE
            }
        }

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvHaveaccount.text = spannableString
        binding.tvHaveaccount.movementMethod = LinkMovementMethod.getInstance()
        binding.tvHaveaccount.highlightColor = Color.TRANSPARENT
    }
}