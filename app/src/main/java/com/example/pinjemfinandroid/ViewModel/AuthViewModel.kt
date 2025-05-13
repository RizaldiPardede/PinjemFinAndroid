package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Api.ApiConfig
import com.example.pinjemfinandroid.Model.LoginRequest
import com.example.pinjemfinandroid.Model.RegisterRequest
import com.example.pinjemfinandroid.Model.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthViewModel: ViewModel() {
    private val _registerResult = MutableLiveData<TokenResponse>()
    val registerResult: LiveData<TokenResponse> = _registerResult

    private val _loginResult = MutableLiveData<TokenResponse>()
    val loginResult: LiveData<TokenResponse> = _loginResult

    private val _registerError = MutableLiveData<String>()
    val registerError: LiveData<String> = _registerError

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

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
                    _loginError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {

                _loginError.value = "Register error: ${t.message}"
            }
        })
    }
}