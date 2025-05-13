package com.example.pinjemfinandroid.Api

import com.example.pinjemfinandroid.Model.SimulasiRequest
import com.example.pinjemfinandroid.Model.SimulasiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PengajuanService {
    @POST("customer/getSimulasiNoAuth")
    fun getSimulasi(@Body request: SimulasiRequest): Call<SimulasiResponse>
}