package com.example.pinjemfinandroid.Utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object ConfirmationUtils {

    fun showConfirmationDialog(
        context: Context,
        message: String,
        onConfirmed: () -> Unit,
        onCancelled: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi")
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onConfirmed() }
            .setNegativeButton("No") { _, _ -> onCancelled() }
            .setCancelable(false) // Tidak bisa dibatalkan dengan menekan di luar dialog
            .show()
    }
}