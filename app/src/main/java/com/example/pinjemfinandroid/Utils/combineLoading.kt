package com.example.pinjemfinandroid.Utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun combineLoading(vararg sources: LiveData<Boolean>): LiveData<Boolean> {
    val result = MediatorLiveData<Boolean>()
    val values = mutableMapOf<LiveData<Boolean>, Boolean>()

    sources.forEach { values[it] = false }

    sources.forEach { source ->
        result.addSource(source) { value ->
            values[source] = value
            result.value = values.values.any { it }
        }
    }
    return result
}