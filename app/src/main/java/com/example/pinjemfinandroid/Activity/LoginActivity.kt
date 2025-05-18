package com.example.pinjemfinandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var preferenceHelper: PreferenceHelper

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonloggin.backgroundTintList = null
        binding.buttonsignup.backgroundTintList = null
        animation()
        preferenceHelper = PreferenceHelper(this)
        auth = FirebaseAuth.getInstance()

        binding.buttonsignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonloggin.setOnClickListener {
            authViewModel.PostLoginUser(binding.etEmail.text.toString(),binding.etPassword.text.toString())
            // Observasi hasil pendaftaran
            authViewModel.loginResult.observe(this) { tokenResponse ->
                // Ketika berhasil
                tokenResponse?.let {

                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    it.token?.let { it1 -> preferenceHelper.setString("token", it1) }

                    startActivity(Intent(this, DashboardActivity::class.java))
                }
            }

            authViewModel.loginError.observe(this) { errorMessage ->
                // Ketika gagal
                errorMessage?.let {
                    Toast.makeText(this, "Email atau Password Salah", Toast.LENGTH_SHORT).show()
                }
            }

        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // pastikan sudah ada di strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnGoogleSign.setOnClickListener{
            signInGoogle()
        }

        authViewModel.cekEmailResult.observe(this) {
            if (it.message == "Email already used") {
                FirebaseAuth.getInstance().currentUser?.getIdToken(false)
                    ?.addOnCompleteListener { task ->
                        val firebaseToken = task.result?.token
                        firebaseToken?.let { token ->
                            authViewModel.PostLoginWithGoogleUser(token)
                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                it1.displayName?.let { it2 -> it1.email?.let { it3 ->
                                    observeloginGoogle(
                                        it3, it2)
                                } }
                            }
                        }
                    }
            } else if (it.message == "Can Register") {
                val intent = Intent(this, CreatePasswordAuthGoogleActivity::class.java)
                intent.putExtra("firebaseEmail", FirebaseAuth.getInstance().currentUser?.email)
                intent.putExtra("firebaseUsername", FirebaseAuth.getInstance().currentUser?.displayName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Coba Login Lagi", Toast.LENGTH_SHORT).show()
            }
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
    private fun signInGoogle() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val email = account.email
                if (email != null) {
                    authViewModel.PostCekEmail(email)
                }
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("LoginActivity", "signInWithCredential:success. User: ${user?.email}")
                    // Bisa lanjut ke main activity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun observeloginGoogle(email: String,username:String){

        authViewModel.loginGoogleResult.observe(this){
            val intent = Intent(this, DashboardActivity::class.java)
            it.token?.let { it1 -> preferenceHelper.setString("token", it1) }
            preferenceHelper.setString("email", email)
            preferenceHelper.setString("username", username)
            startActivity(intent)
        }

        authViewModel.loginGoogleError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

    }
}