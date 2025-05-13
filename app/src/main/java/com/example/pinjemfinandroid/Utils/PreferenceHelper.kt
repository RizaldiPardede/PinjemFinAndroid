package com.example.pinjemfinandroid.Utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val PREF_NAME = "my_shared_pref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Simpan data String
    fun setString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    // Simpan data Boolean
    fun setBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    // Simpan data Int
    fun setInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }

    // Hapus satu data
    fun remove(key: String) {
        sharedPref.edit().remove(key).apply()
    }

    // Hapus semua data
    fun clear() {
        sharedPref.edit().clear().apply()
    }

}