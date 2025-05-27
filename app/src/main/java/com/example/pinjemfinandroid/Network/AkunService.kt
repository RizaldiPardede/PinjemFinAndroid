package com.example.pinjemfinandroid.Network

import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.Model.DetailCustomerResponse
import com.example.pinjemfinandroid.Model.GetAllImageRequest
import com.example.pinjemfinandroid.Model.GetAllImageResponseItem
import com.example.pinjemfinandroid.Model.InformasiPengajuanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AkunService {

    @POST("customer/AddDetailAkun")
    fun addDetailAccount(@Body request: DetailCustomerRequest): Call<DetailCustomerResponse>

    @POST("customer/getInformasiPengajuan")
    fun getInformasiPlafonCustomer(): Call<InformasiPengajuanResponse>

    @POST("customer/getAllImageCustomer")
    fun getAllImageCustomer(): Call<List<GetAllImageResponseItem?>?>
}