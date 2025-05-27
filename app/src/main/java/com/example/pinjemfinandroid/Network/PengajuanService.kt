package com.example.pinjemfinandroid.Network

import PengajuanResponseItem
import com.example.pinjemfinandroid.Model.GetAllImageRequest
import com.example.pinjemfinandroid.Model.GetAllImageResponseItem
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.PengajuanRequest
import com.example.pinjemfinandroid.Model.PengajuanResponse
import com.example.pinjemfinandroid.Model.SimulasiRequest
import com.example.pinjemfinandroid.Model.SimulasiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PengajuanService {
    @POST("customer/getSimulasiNoAuth")
    fun getSimulasiNoAuth(@Body request: SimulasiRequest): Call<SimulasiResponse>
    @POST("customer/getSimulasi")
    fun getSimulasi(@Body request: SimulasiRequest): Call<SimulasiResponse>
    @POST("pengajuan/CreatePengajuan")
    fun postPengajuan(@Body request: PengajuanRequest): Call<PengajuanResponse>

    @POST("customer/CekUpdateAkun")
    fun cekUpdateAkun(): Call<MessageResponse>

    @GET("pengajuan/getAllPengajuan")
    fun getAllPengajuan(): Call<List<PengajuanResponseItem>>


}