package com.example.pinjemfinandroid.Network

import com.example.pinjemfinandroid.Model.GetProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DokumenService {

    @Multipart
    @POST("dokumen/uploadImage")
    fun uploadImage(
        @Part("imageType") imageType: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<Map<String, String>>

    @POST("dokumen/getProfileImage")
    fun getProfileImage(): Call<GetProfileResponse>

}