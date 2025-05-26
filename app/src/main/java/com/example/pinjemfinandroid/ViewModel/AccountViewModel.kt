package com.example.pinjemfinandroid.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Network.ApiConfig
import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.Model.DetailCustomerResponse
import com.example.pinjemfinandroid.Utils.AlertEvent
import com.example.pinjemfinandroid.Utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountViewModel : ViewModel() {

    private val _addDetailResult = MutableLiveData<DetailCustomerResponse>()
    val addDetailResult: LiveData<DetailCustomerResponse> = _addDetailResult

    private val _addDetailError = MutableLiveData<String>()
    val addDetailError: LiveData<String> = _addDetailError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _alertEvent = MutableLiveData<AlertEvent>()
    val alertEvent: LiveData<AlertEvent> = _alertEvent


    fun addDetailAccount(detailcustomer: DetailCustomerRequest, token:String) {

        val call = ApiConfig.getAkunService(token).addDetailAccount(detailcustomer)
        _isLoading.value = true
        call.enqueue(object : Callback<DetailCustomerResponse> {
            override fun onResponse(
                call: Call<DetailCustomerResponse>,
                response: Response<DetailCustomerResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _addDetailResult.value = response.body()!!
                    _alertEvent.value =AlertEvent.ShowSuccess("Berhasil Buat detail")
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    ErrorHandler.handleErrorResponse(errorBodyString,
                        onError = { message -> _addDetailError.value = message },
                        onAlert = { alert -> _alertEvent.value = alert }
                    )
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<DetailCustomerResponse>, t: Throwable) {

                val errorBodyString = t.message
                ErrorHandler.handleErrorResponse(errorBodyString,
                    onError = { message -> _addDetailError.value = message },
                    onAlert = { alert -> _alertEvent.value = alert }
                )
                _isLoading.value = false
            }
        })
    }
}