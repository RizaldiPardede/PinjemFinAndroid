package com.example.pinjemfinandroid.Utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.pinjemfinandroid.R

object LottieAlertHelper {
    fun showSuccess(context: Context, message: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<LottieAnimationView>(R.id.lottieAnim)
            .setAnimation(R.raw.success_alert) // success.json di res/raw


        dialogView.findViewById<TextView>(R.id.tvTitle)?.text = message
        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showError(context: Context, message: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<LottieAnimationView>(R.id.lottieAnim)
            .setAnimation(R.raw.error_alert) // error.json di res/raw

        dialogView.findViewById<TextView>(R.id.tvTitle)?.text = message
        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}