package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Network.ApiConfig
import com.example.pinjemfinandroid.Model.EmailActivationRequest
import com.example.pinjemfinandroid.Model.EmailCekRequest
import com.example.pinjemfinandroid.Model.LoginGoogleRequest
import com.example.pinjemfinandroid.Model.LoginRequest
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.ProfileResponse
import com.example.pinjemfinandroid.Model.RegisterRequest
import com.example.pinjemfinandroid.Model.TokenResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthViewModel: ViewModel() {
    private val _registerResult = MutableLiveData<TokenResponse>()
    val registerResult: LiveData<TokenResponse> = _registerResult

    private val _registerError = MutableLiveData<String>()
    val registerError: LiveData<String> = _registerError

    private val _loginResult = MutableLiveData<TokenResponse>()
    val loginResult: LiveData<TokenResponse> = _loginResult

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    private val _cekEmailResult = MutableLiveData<MessageResponse>()
    val cekEmailResult: LiveData<MessageResponse> = _cekEmailResult

    private val _cekEmailError = MutableLiveData<String>()
    val cekEmailError: LiveData<String> = _cekEmailError

    private val _loginGoogleResult = MutableLiveData<TokenResponse>()
    val loginGoogleResult: LiveData<TokenResponse> = _loginGoogleResult

    private val _loginGoogleError = MutableLiveData<String>()
    val loginGoogleError: LiveData<String> = _loginGoogleError

    private val _registerAuthGoogleResult = MutableLiveData<TokenResponse>()
    val registerAuthGoogleResult: LiveData<TokenResponse> = _registerAuthGoogleResult

    private val _registerAuthGoogleError = MutableLiveData<String>()
    val registerAuthGoogleError: LiveData<String> = _registerAuthGoogleError


    private val _emailActivationResult = MutableLiveData<MessageResponse>()
    val emailActivationResult: LiveData<MessageResponse> = _emailActivationResult

    private val _emailActivationError = MutableLiveData<String>()
    val emailActivationError: LiveData<String> = _emailActivationError

    private val _profileResult = MutableLiveData<ProfileResponse>()
    val profileResult: LiveData<ProfileResponse> = _profileResult

    private val _profileError = MutableLiveData<String>()
    val profileError: LiveData<String> = _profileError
    fun PostRegisterUser(username: String, password: String, nama: String) {
        val request = RegisterRequest(password,nama,username)
        val call = ApiConfig.getApiService().Register(request)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _registerResult.value = response.body()!!
                } else {
                    _registerError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {

                _registerError.value = "Register error: ${t.message}"
            }
        })
    }

    fun PostRegisterUserAuthGoogle(username: String, password: String, nama: String) {
        val request = RegisterRequest(password,nama,username)
        val call = ApiConfig.getApiService().registerAuthGoogle(request)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _registerAuthGoogleResult.value = response.body()!!
                } else {

                    _registerAuthGoogleError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {

                _registerAuthGoogleError.value = "Register error: ${t.message}"
            }
        })
    }

    fun PostLoginUser(username: String, password: String){
        val request = LoginRequest(password,username)
        val call = ApiConfig.getApiService().Login(request)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = response.body()!!
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBodyString, MessageResponse::class.java)
                            _loginError.value = errorResponse?.message ?: "Terjadi kesalahan tidak diketahui"
                        } catch (e: Exception) {
                            _loginError.value = "Login gagal: $errorBodyString"
                        }
                    } else {
                        _loginError.value = "Login gagal: Response error kosong"
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {

                _loginError.value = "Login error: ${t.message}"
            }
        })
    }

    fun PostCekEmail(email: String){
        val request = EmailCekRequest(email)
        val call = ApiConfig.getApiService().cekEmail(request)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _cekEmailResult.value = response.body()!!
                } else {
                    _cekEmailError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                _cekEmailError.value = "Register error: ${t.message}"
            }
        })
    }

    fun PostLoginWithGoogleUser(firebaseIdToken:String){
        val request = LoginGoogleRequest(firebaseIdToken)
        val call = ApiConfig.getApiService().loginWithgoogle(request)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _loginGoogleResult.value = response.body()!!
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBodyString, MessageResponse::class.java)
                            _loginError.value = errorResponse?.message ?: "Terjadi kesalahan tidak diketahui"
                        } catch (e: Exception) {
                            _loginError.value = "Login gagal: $errorBodyString"
                        }
                    } else {
                        _loginError.value = "Login gagal: Response error kosong"
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {

                _loginGoogleError.value = "login error: ${t.message}"
            }
        })
    }

    fun PostEmailActivation(tokenActivation:String) {
        val request = EmailActivationRequest(tokenActivation)
        val call = ApiConfig.getApiService().emailActivation(request)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _emailActivationResult.value = response.body()!!
                } else {
                    _emailActivationError.value = "Aktifasi gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                _emailActivationError.value = "Aktifasi error: ${t.message}"
            }
        })
    }



    fun getProfile(token:String){
        val call = ApiConfig.getApiService(token).getProfile()
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _profileResult.value = response.body()!!
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBodyString, MessageResponse::class.java)
                            _profileError.value = errorResponse?.message ?: "Terjadi kesalahan tidak diketahui"
                        } catch (e: Exception) {
                            _profileError.value = "get profile gagal: $errorBodyString"
                        }
                    } else {
                        _profileError.value = "get profile gagal: Response error kosong"
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                _profileError.value = "Login error: ${t.message}"
            }
        })
    }
}