package com.example.pinjemfinandroid.Model

data class GetAllImageResponse(
	val getAllImageResponse: List<GetAllImageResponseItem?>? = null
)

data class Roleimg(
	val idRole: String? = null,
	val namaRole: String? = null
)

data class Branchimg(
	val idBranch: String? = null,
	val longitudeBranch: Any? = null,
	val namaBranch: String? = null,
	val latitudeBranch: Any? = null,
	val alamatBranch: String? = null
)

data class Usersimg(
	val password: String? = null,
	val role: Roleimg? = null,
	val nama: String? = null,
	val idUser: String? = null,
	val isActive: Boolean? = null,
	val email: String? = null
)

data class Plafonimg(
	val idPlafon: String? = null,
	val jenisPlafon: String? = null,
	val jumlahPlafon: Any? = null,
	val bunga: Any? = null
)

data class UserCustomer(
	val sisaPlafon: Any? = null,
	val noRek: String? = null,
	val gaji: String? = null,
	val tempatTglLahir: String? = null,
	val branch: Branchimg? = null,
	val users: Usersimg? = null,
	val alamat: String? = null,
	val statusRumah: String? = null,
	val nik: String? = null,
	val idUserCustomer: String? = null,
	val pekerjaan: String? = null,
	val namaIbuKandung: String? = null,
	val noTelp: String? = null,
	val plafon: Plafonimg? = null
)

data class GetAllImageResponseItem(
	val createdAt: String? = null,
	val userCustomer: UserCustomer? = null,
	val imageUrl: String? = null,
	val id: String? = null,
	val imageType: String? = null
)

