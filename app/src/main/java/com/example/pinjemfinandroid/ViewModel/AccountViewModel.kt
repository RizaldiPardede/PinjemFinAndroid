package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Network.ApiConfig
import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.Model.DetailCustomerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountViewModel : ViewModel() {

    private val _addDetailResult = MutableLiveData<DetailCustomerResponse>()
    val addDetailResult: LiveData<DetailCustomerResponse> = _addDetailResult

    private val _addDetailError = MutableLiveData<String>()
    val addDetailError: LiveData<String> = _addDetailError



    fun addDetailAccount(detailcustomer: DetailCustomerRequest, token:String) {

        val call = ApiConfig.getAkunService(token).addDetailAccount(detailcustomer)
        call.enqueue(object : Callback<DetailCustomerResponse> {
            override fun onResponse(
                call: Call<DetailCustomerResponse>,
                response: Response<DetailCustomerResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _addDetailResult.value = response.body()!!
                } else {
                    _addDetailError.value = "Register gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<DetailCustomerResponse>, t: Throwable) {

                _addDetailError.value = "Register error: ${t.message}"
            }
        })
    }
}