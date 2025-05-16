package com.example.pinjemfinandroid.Api

import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.Model.DetailCustomerResponse
import com.example.pinjemfinandroid.Model.InformasiPengajuanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AkunService {

    @POST("customer/AddDetailAkun")
    fun addDetailAccount(@Body request: DetailCustomerRequest): Call<DetailCustomerResponse>

    @POST("customer/getInformasiPengajuan")
    fun getInformasiPlafonCustomer(): Call<InformasiPengajuanResponse>
}