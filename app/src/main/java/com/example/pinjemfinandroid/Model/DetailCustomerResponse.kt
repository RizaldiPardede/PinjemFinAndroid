package com.example.pinjemfinandroid.Model

data class DetailCustomerResponse(
	val sisaPlafon: Any? = null,
	val noRek: String? = null,
	val gaji: String? = null,
	val tempatTglLahir: String? = null,
	val branch: Branch? = null,
	val users: Users? = null,
	val alamat: String? = null,
	val statusRumah: String? = null,
	val nik: String? = null,
	val idUserCustomer: String? = null,
	val pekerjaan: String? = null,
	val namaIbuKandung: String? = null,
	val noTelp: String? = null,
	val plafon: Plafon? = null
)

data class Role(
	val idRole: String? = null,
	val namaRole: String? = null
)

data class Plafon(
	val idPlafon: String? = null,
	val jenisPlafon: String? = null,
	val jumlahPlafon: Any? = null,
	val bunga: Any? = null
)

data class Users(
	val password: String? = null,
	val role: Role? = null,
	val nama: String? = null,
	val idUser: String? = null,
	val email: String? = null
)

data class Branch(
	val idBranch: String? = null,
	val longitudeBranch: Any? = null,
	val namaBranch: String? = null,
	val latitudeBranch: Any? = null,
	val alamatBranch: String? = null
)

