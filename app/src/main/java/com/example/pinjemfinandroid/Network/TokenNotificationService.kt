package com.example.pinjemfinandroid.Network

import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.TokenNotifRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenNotificationService {

    @POST("tokenNotifikasi/addTokenNotifikasi")
    fun addTokenNotif(@Body request: TokenNotifRequest): Call<MessageResponse>

    @POST("tokenNotifikasi/clearUserCustomerNotifikasi")
    fun clearUserCustomerNotifikasi(@Body request: TokenNotifRequest): Call<MessageResponse>
}