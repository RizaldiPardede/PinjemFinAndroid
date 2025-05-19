package com.example.pinjemfinandroid.Network
import com.example.pinjemfinandroid.Model.EmailActivationRequest
import com.example.pinjemfinandroid.Model.EmailCekRequest
import com.example.pinjemfinandroid.Model.LoginGoogleRequest
import com.example.pinjemfinandroid.Model.LoginRequest
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.ProfileResponse
import com.example.pinjemfinandroid.Model.RegisterRequest
import com.example.pinjemfinandroid.Model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/registerAkunCustomer")
    fun Register(@Body request: RegisterRequest): Call<TokenResponse>

    @POST("auth/login")
    fun Login(@Body request: LoginRequest): Call<TokenResponse>

    @POST("auth/emailActivation")
    fun emailActivation(@Body request: EmailActivationRequest): Call<MessageResponse>

    @POST("customer/cekEmailCustomer")
    fun cekEmail(@Body request: EmailCekRequest): Call<MessageResponse>

    @POST("auth/loginWithgoogle")
    fun loginWithgoogle(@Body request: LoginGoogleRequest): Call<TokenResponse>

    @POST("auth/registerAuthGoogle")
    fun registerAuthGoogle(@Body request: RegisterRequest): Call<TokenResponse>

    @GET("customer/getProfile")
    fun getProfile(): Call<ProfileResponse>



}