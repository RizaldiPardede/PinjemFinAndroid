package com.example.pinjemfinandroid.Fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//untuk mengirim data live dari fragment dashboard ke transaction fragment
class SharedViewModel: ViewModel() {
    private val _amount = MutableLiveData<Double>()
    private val _tenor = MutableLiveData<Int>()

    val amount: LiveData<Double> get() = _amount
    val tenor: LiveData<Int> get() = _tenor

    private val _refreshHistory = MutableLiveData<Unit>()
    val refreshHistory: LiveData<Unit> = _refreshHistory

    private val _refreshTrigger = MutableLiveData<Unit>()
    val refreshTrigger: LiveData<Unit> get() = _refreshTrigger

    fun setAmount(value: Double) {
        _amount.value = value
    }

    fun setTenor(value: Int) {
        _tenor.value = value
    }

    fun notifyRefresh() {
        _refreshHistory.value = Unit
    }



    fun triggerRefresh() {
        _refreshTrigger.value = Unit
    }
}