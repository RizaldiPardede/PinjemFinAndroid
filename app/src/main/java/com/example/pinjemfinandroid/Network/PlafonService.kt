package com.example.pinjemfinandroid.Network

import com.example.pinjemfinandroid.Model.ListPlafonResponse
import com.example.pinjemfinandroid.Model.ListPlafonResponseItem
import com.example.pinjemfinandroid.Model.SimulasiRequest
import com.example.pinjemfinandroid.Model.SimulasiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PlafonService {
    @GET("plafon/getAllPlafon")
    fun getAllPlafon(): Call<List<ListPlafonResponseItem>>
}