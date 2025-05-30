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
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.Local.NotificationViewModel
import com.example.pinjemfinandroid.Local.UserRoomViewModel
import com.example.pinjemfinandroid.Local.dao.UserDataDao
import com.example.pinjemfinandroid.Local.entity.UserData
import com.example.pinjemfinandroid.Model.ProfileResponse
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.Utils.combineLoading
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val userRoomViewModel: UserRoomViewModel by viewModels()
    private lateinit var preferenceHelper: PreferenceHelper
    private val notificationviewModel: NotificationViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    var emailForLoginGoogle: String? = null
    var usernameForLoginGoogle: String? = null
    private var pendingIdToken: String? = null


    @Inject
    lateinit var userDataDao: UserDataDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonloggin.backgroundTintList = null
        binding.buttonsignup.backgroundTintList = null
        animation()
        isLoading()
        forgotpassword()
        preferenceHelper = PreferenceHelper(this)
        auth = FirebaseAuth.getInstance()
        preferenceHelper.clear()
        userRoomViewModel.clearUsers()
        notificationviewModel.deleteAllNotifications()

        binding.buttonsignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonloggin.setOnClickListener {
            authViewModel.PostLoginUser(binding.etEmail.text.toString(),binding.etPassword.text.toString())

        }
        observeLoginResult()
        observeloginGoogle()
        observeEmailPassword()
        observeForRoom()
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
//                pendingIdToken?.let { it1 -> firebaseAuthWithGoogle(it1) }
//                FirebaseAuth.getInstance().currentUser?.getIdToken(false)
//                    ?.addOnCompleteListener { task ->
//                        val firebaseToken = task.result?.token
//                        firebaseToken?.let { token ->
//                            authViewModel.PostLoginWithGoogleUser(token)
//                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
//                                it1.displayName?.let { it2 -> it1.email?.let { it3 ->
//                                    usernameForLoginGoogle = it2
//                                    emailForLoginGoogle = it3
//                                } }
//                            }
//                        }
//                    }
                pendingIdToken?.let { token ->
                    firebaseAuthWithGoogle(token)
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

    private fun signInGoogle() {
        auth.signOut()
        preferenceHelper.clear()
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun observeLoginResult() {
        // Observasi hasil pendaftaran
        authViewModel.loginResult.observe(this) { tokenResponse ->
            if (tokenResponse.token.isNullOrEmpty()){

                Toast.makeText(this, "Akun Belum diverifikasi", Toast.LENGTH_SHORT).show()
            }

            else{
                tokenResponse?.let {

                    Toast.makeText(this, "Logoin Successful!", Toast.LENGTH_SHORT).show()
                    it.token?.let { it1 -> preferenceHelper.setString("token", it1) }
                    it.token?.let { it1 ->
                        authViewModel.getProfile(it1)
                        authViewModel.getUser(it1)
                    }

                    //taruh disini untuk get profile

                }
            }

        }

        authViewModel.loginError.observe(this) { errorMessage ->
            // Ketika gagal
            errorMessage?.let {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val email = account.email
                pendingIdToken = account.idToken
                if (email != null) {
                    authViewModel.PostCekEmail(email)
                }
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
                    val user = auth.currentUser
                    user?.getIdToken(false)?.addOnCompleteListener { tokenTask ->
                        val firebaseToken = tokenTask.result?.token
                        firebaseToken?.let { token ->
                            authViewModel.PostLoginWithGoogleUser(token)

                            // Simpan email & username jika perlu
                            user.displayName?.let { usernameForLoginGoogle = it }
                            user.email?.let { emailForLoginGoogle = it }
                        }
                    }
                } else {
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun observeloginGoogle(){

        authViewModel.loginGoogleResult.observe(this){

            it.token?.let { it1 -> preferenceHelper.setString("token", it1) }

            it.token?.let { it1 -> authViewModel.getProfile(it1) }

            emailForLoginGoogle?.let { it2 -> preferenceHelper.setString("email", it2) }
            usernameForLoginGoogle?.let { it2 -> preferenceHelper.setString("username", it2) }

        }

        authViewModel.loginGoogleError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

    }

    private fun observeForRoom(){

        authViewModel.profileResult.observe(this){
            if (it != null) {
                // Pastikan data penting tidak null, misal idUser dan users
                val idUser = it.users?.idUser
                if (!idUser.isNullOrEmpty()) {
                    // Mapping ke entity Room
                    val entity = mapProfileResponseToEntity(it)

                    // Insert ke database Room (jalan di background thread)
                    lifecycleScope.launch {
                        if (entity != null) {
                            userDataDao.clearAll()
                            userDataDao.insertUser(entity)
                            Log.d("RoomInsert", "Data berhasil dimasukkan ke Room: ${entity.nama}")
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        }



                    }
                } else {
                    // Data kosong, tidak insert ke Room
                    Log.d("RoomInsert", "ProfileResponse users.idUser is null or empty, skip insert")
                }
            } else {
                // Response null, skip insert
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                Log.d("RoomInsert", "ProfileResponse is null, skip insert")
            }
        }
        authViewModel.profileError.observe(this){
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            Log.e("ProfileError", "Error getting profile: $it")
        }
    }

    fun observeEmailPassword(){
        authViewModel.getUserResult.observe(this){
            it.nama?.let { it1 ->
                preferenceHelper.setString("username", it1) }
            it.email?.let{
                preferenceHelper.setString("email",it)
            }
        }
        authViewModel.getUserError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

    }

    fun mapProfileResponseToEntity(response:ProfileResponse): UserData? {
        return response.users?.email?.let {
            UserData(
                // id auto generate, jangan diisi di sini
                nama = response.users?.nama,
                email = it,
                nama_role = response.users?.role?.namaRole,
                tempat_tgl_lahir = response.tempatTglLahir,
                no_telp = response.noTelp,
                alamat = response.alamat,
                nik = response.nik,
                nama_ibu_kandung = response.namaIbuKandung,
                pekerjaan = response.pekerjaan,
                gaji = response.gaji,
                no_rek = response.noRek,
                status_rumah = response.statusRumah,
                jenis_plafon = response.plafon?.jenisPlafon,
                jumlah_plafon = (response.plafon?.jumlahPlafon as? Double) ?: (response.plafon?.jumlahPlafon as? Float)?.toDouble(),
                bunga = (response.plafon?.bunga as? Double) ?: (response.plafon?.bunga as? Float)?.toDouble(),
                nama_branch = response.branch?.namaBranch,
                alamat_branch = response.branch?.alamatBranch,
                latitude_branch = (response.branch?.latitudeBranch as? Double) ?: (response.branch?.latitudeBranch as? Float)?.toDouble(),
                longitude_branch = (response.branch?.longitudeBranch as? Double) ?: (response.branch?.longitudeBranch as? Float)?.toDouble(),
                sisa_plafon = (response.sisaPlafon as? Double) ?: (response.sisaPlafon as? Float)?.toDouble()
            )
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

    private fun forgotpassword(){
        val fullText = "Lupa Password?"
        val spannableString = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
                // Aksi lain bisa ditaruh di sini, seperti membuka halaman lain
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false // hilangkan underline
                ds.color = Color.BLUE // atur warna jika mau
            }
        }

        spannableString.setSpan(clickableSpan, 0, fullText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvForgotPassword.text = spannableString
        binding.tvForgotPassword.movementMethod = LinkMovementMethod.getInstance()
        binding.tvForgotPassword.highlightColor = Color.TRANSPARENT
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