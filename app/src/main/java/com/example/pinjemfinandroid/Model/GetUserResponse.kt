package com.example.pinjemfinandroid.Model

data class GetUserResponse(
	val password: String? = null,
	val role: Role? = null,
	val nama: String? = null,
	val idUser: String? = null,
	val isActive: Boolean? = null,
	val email: String? = null
)

data class RoleUser(
	val idRole: String? = null,
	val namaRole: String? = null
)

