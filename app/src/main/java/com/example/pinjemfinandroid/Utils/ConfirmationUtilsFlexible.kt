package com.example.pinjemfinandroid.Utils

import android.app.AlertDialog
import android.content.Context

object ConfirmationUtilsFlexible {

    fun showFlexibleDialog(
        context: Context,
        title: String = "Konfirmasi",
        message: String,
        positiveButtonText: String = "Yes",
        negativeButtonText: String = "No",
        onConfirmed: () -> Unit,
        onCancelled: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onConfirmed() }
            .setNegativeButton(negativeButtonText) { _, _ -> onCancelled() }
            .setCancelable(false)
            .show()
    }
}