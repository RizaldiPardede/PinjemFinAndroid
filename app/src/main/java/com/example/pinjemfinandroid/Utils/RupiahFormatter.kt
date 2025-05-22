package com.example.pinjemfinandroid.Utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object RupiahFormatter {
    fun format(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("in", "ID")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }

        val decimalFormat = DecimalFormat("#,##0.00", symbols)
        return "Rp ${decimalFormat.format(value)}"
    }

    fun format(value: Int): String = format(value.toDouble())

    fun deformat(formattedValue: String): Double {
        val cleanString = formattedValue
            .replace("Rp", "")
            .replace(" ", "")
            .replace(".", "")
            .replace(",", ".")

        return try {
            cleanString.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

}