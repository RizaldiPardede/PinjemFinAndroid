package com.example.pinjemfinandroid.Api
import com.example.pinjemfinandroid.Model.LoginRequest
import com.example.pinjemfinandroid.Model.RegisterRequest
import com.example.pinjemfinandroid.Model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/registerAkunCustomer")
    fun Register(@Body request: RegisterRequest): Call<TokenResponse>

    @POST("auth/login")
    fun Login(@Body request:LoginRequest ): Call<TokenResponse>

}