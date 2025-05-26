package com.example.pinjemfinandroid.Utils

import com.example.pinjemfinandroid.Model.MessageResponse
import com.google.gson.Gson

object ErrorHandler {
    fun handleErrorResponse(
        errorBodyString: String?,
        onError: (String) -> Unit,
        onAlert: (AlertEvent) -> Unit
    ) {
        if (!errorBodyString.isNullOrEmpty()) {
            try {
                val errorResponse = Gson().fromJson(errorBodyString, MessageResponse::class.java)
                val message = errorResponse?.message ?: "Terjadi kesalahan tidak diketahui"
                onError(message)
                onAlert(AlertEvent.ShowError(message = message))
            } catch (e: Exception) {
                val fallbackMessage = "gagal: $errorBodyString"
                onError(fallbackMessage)
                onAlert(AlertEvent.ShowError(message = fallbackMessage))
            }
        } else {
            val emptyMessage = "gagal: Response error kosong"
            onError(emptyMessage)
            onAlert(AlertEvent.ShowError(message = emptyMessage))
        }
    }
}