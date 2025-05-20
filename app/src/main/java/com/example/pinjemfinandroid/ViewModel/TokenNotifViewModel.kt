package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Model.MessageResponse
import com.example.pinjemfinandroid.Model.PengajuanRequest
import com.example.pinjemfinandroid.Model.PengajuanResponse
import com.example.pinjemfinandroid.Model.TokenNotifRequest
import com.example.pinjemfinandroid.Network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TokenNotifViewModel: ViewModel() {
    private val _addTokenResult = MutableLiveData<MessageResponse>()
    val addTokenResult: LiveData<MessageResponse> = _addTokenResult

    private val _addTokenError = MutableLiveData<String>()
    val addTokenError: LiveData<String> = _addTokenError

    private val _cleanTokenResult = MutableLiveData<MessageResponse>()
    val cleanTokenResult: LiveData<MessageResponse> = _cleanTokenResult

    private val _cleanTokenError = MutableLiveData<String>()
    val cleanTokenError: LiveData<String> = _cleanTokenError

    fun postAddTokenNotif(tokenNotif:String, token:String) {
        val request = TokenNotifRequest(tokenNotif)
        val call = ApiConfig.gettokenNotifikasiservice(token).addTokenNotif(request)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _addTokenResult.value = response.body()!!
                } else {
                    _addTokenError.value = "Add token notif gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                _addTokenError.value = "Add token notif error: ${t.message}"
            }
        })
    }

    fun postCleanTokenNotif(tokenNotif:String, token:String) {
        val request = TokenNotifRequest(tokenNotif)
        val call = ApiConfig.gettokenNotifikasiservice(token).clearUserCustomerNotifikasi(request)
        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _cleanTokenResult.value = response.body()!!
                } else {
                    _cleanTokenError.value = "Clean token notif gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                _cleanTokenError.value = "Clean token notif error: ${t.message}"
            }
        })
    }
}