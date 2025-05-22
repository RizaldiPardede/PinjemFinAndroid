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

    fun setAmount(value: Double) {
        _amount.value = value
    }

    fun setTenor(value: Int) {
        _tenor.value = value
    }
}