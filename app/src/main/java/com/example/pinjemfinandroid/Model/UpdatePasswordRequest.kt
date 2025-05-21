package com.example.pinjemfinandroid.Model

data class UpdatePasswordRequest(
	val oldPassword: String? = null,
	val newPassword: String? = null
)

