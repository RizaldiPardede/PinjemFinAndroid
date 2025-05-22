package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Model.ForgotPasswordRequest
import com.example.pinjemfinandroid.Model.ListPlafonResponse
import com.example.pinjemfinandroid.Model.ListPlafonResponseItem
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Network.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlafonViewModel: ViewModel() {
    private val _getAllPlafonResult = MutableLiveData<List<ListPlafonResponseItem>>()
    val getAllPlafonResult: LiveData<List<ListPlafonResponseItem>> = _getAllPlafonResult

    private val _getAllPlafonError = MutableLiveData<String>()
    val getAllPlafonError: LiveData<String> = _getAllPlafonError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllPlafon(){

        val call = ApiConfig.getPlafon().getAllPlafon()
        _isLoading.value = true
        call.enqueue(object : Callback<List<ListPlafonResponseItem>> {
            override fun onResponse(
                call: Call<List<ListPlafonResponseItem>>,
                response: Response<List<ListPlafonResponseItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _getAllPlafonResult.value = response.body()!!
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBodyString, MessageResponse::class.java)
                            _getAllPlafonError.value = errorResponse?.message ?: "Terjadi kesalahan tidak diketahui"
                        } catch (e: Exception) {
                            _getAllPlafonError.value = "gagal: $errorBodyString"
                        }
                    } else {
                        _getAllPlafonError.value = "gagal: Response error kosong"
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<ListPlafonResponseItem>>, t: Throwable) {

                _getAllPlafonError.value = "error: ${t.message}"
                _isLoading.value = false
            }
        })
    }

}