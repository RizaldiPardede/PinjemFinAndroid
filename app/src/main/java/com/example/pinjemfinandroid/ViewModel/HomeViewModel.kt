package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Api.ApiConfig
import com.example.pinjemfinandroid.Model.RegisterRequest
import com.example.pinjemfinandroid.Model.SimulasiRequest
import com.example.pinjemfinandroid.Model.SimulasiResponse
import com.example.pinjemfinandroid.Model.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private val _simulasiResult = MutableLiveData<SimulasiResponse>()
    val simulasiResult: LiveData<SimulasiResponse> = _simulasiResult

    private val _simulasiError = MutableLiveData<String>()
    val registerError: LiveData<String> = _simulasiError

    fun getSimulasiPengajuan(amount: Double, tenor: Int) {

        val request = SimulasiRequest(amount,tenor)
        val call = ApiConfig.getPengajuanService().getSimulasi(request)
        call.enqueue(object : Callback<SimulasiResponse> {
            override fun onResponse(
                call: Call<SimulasiResponse>,
                response: Response<SimulasiResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _simulasiResult.value = response.body()!!
                } else {
                    _simulasiError.value = "Simulasi gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<SimulasiResponse>, t: Throwable) {

                _simulasiError.value = "Simulasi error: ${t.message}"
            }
        })
    }

}