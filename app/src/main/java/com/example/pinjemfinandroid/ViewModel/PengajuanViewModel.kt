package com.example.pinjemfinandroid.ViewModel

import PengajuanResponseItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Network.ApiConfig
import com.example.pinjemfinandroid.Model.InformasiPengajuanResponse
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.PengajuanRequest
import com.example.pinjemfinandroid.Model.PengajuanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengajuanViewModel: ViewModel() {
    private val _pengajuanResponse = MutableLiveData<PengajuanResponse>()
    val pengajuanResponse: LiveData<PengajuanResponse> = _pengajuanResponse

    private val _pengajuanError = MutableLiveData<String>()
    val pengajuanError: LiveData<String> = _pengajuanError

    private val _cekUpdate = MutableLiveData<MessageResponse>()
    val cekUpdate: LiveData<MessageResponse> = _cekUpdate

    private val _cekUpdateError = MutableLiveData<String>()
    val cekUpdateError: LiveData<String> = _cekUpdateError

    private val _informasiPengajuan = MutableLiveData<InformasiPengajuanResponse>()
    val informasiPengajuan: LiveData<InformasiPengajuanResponse> = _informasiPengajuan

    private val _informasiPengajuanError = MutableLiveData<String>()
    val informasiPengajuanError: LiveData<String> = _informasiPengajuanError


    private val _ListpengajuanResult = MutableLiveData<List<PengajuanResponseItem>>()
    val ListpengajuanResult: LiveData<List<PengajuanResponseItem>> = _ListpengajuanResult

    private val _ListpengajuanError = MutableLiveData<String>()
    val ListpengajuanError: LiveData<String> = _ListpengajuanError
    fun postPengajuan(pengajuanRequest: PengajuanRequest, token:String) {

        val call = ApiConfig.getPengajuanService(token).postPengajuan(pengajuanRequest)
        call.enqueue(object : Callback<PengajuanResponse> {
            override fun onResponse(
                call: Call<PengajuanResponse>,
                response: Response<PengajuanResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _pengajuanResponse.value = response.body()!!
                } else {
                    _pengajuanError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<PengajuanResponse>, t: Throwable) {

                _pengajuanError.value = "Register error: ${t.message}"
            }
        })
    }

    fun cekUpdateAkun(token:String) {

        val call = ApiConfig.getPengajuanService(token).cekUpdateAkun()
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _cekUpdate.value = response.body()!!
                } else {
                    _cekUpdateError.value = "Kesalahan: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                _cekUpdateError.value = "Kesalahan: ${t.message}"
            }
        })
    }

    fun getInformasiPlafonCustomer(token:String) {

        val call = ApiConfig.getAkunService(token).getInformasiPlafonCustomer()
        call.enqueue(object : Callback<InformasiPengajuanResponse> {
            override fun onResponse(
                call: Call<InformasiPengajuanResponse>,
                response: Response<InformasiPengajuanResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _informasiPengajuan.value = response.body()!!
                } else {
                    _informasiPengajuanError.value = "Kesalahan: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<InformasiPengajuanResponse>, t: Throwable) {

                _informasiPengajuanError.value = "Kesalahan: ${t.message}"
            }
        })
    }


    fun getAllPengajuan(token:String) {

        val call = ApiConfig.getPengajuanService(token).getAllPengajuan()
        call.enqueue(object : Callback<List<PengajuanResponseItem>> {
            override fun onResponse(
                call: Call<List<PengajuanResponseItem>>,
                response: Response<List<PengajuanResponseItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _ListpengajuanResult.value = response.body()!!
                } else {
                    _ListpengajuanError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<PengajuanResponseItem>>, t: Throwable) {

                _ListpengajuanError.value = "Register error: ${t.message}"
            }
        })
    }
}