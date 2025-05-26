package com.example.pinjemfinandroid.Utils

sealed class AlertEvent{
    data class ShowSuccess( val message: String) : AlertEvent()
    data class ShowError( val message: String) : AlertEvent()
    object Dismiss : AlertEvent()
}