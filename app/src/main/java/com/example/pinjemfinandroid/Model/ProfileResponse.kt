package com.example.pinjemfinandroid.Model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
	@SerializedName("sisa_plafon")
	val sisaPlafon: Any? = null,
	@SerializedName("no_rek")
	val noRek: String? = null,
	val gaji: String? = null,
	@SerializedName("tempat_tgl_lahir")
	val tempatTglLahir: String? = null,
	val branch: Branchprof? = null,
	val users: Usersprof? = null,
	val alamat: String? = null,
	@SerializedName("status_rumah")
	val statusRumah: String? = null,
	val nik: String? = null,
	@SerializedName("id_user_customer")
	val idUserCustomer: String? = null,
	val pekerjaan: String? = null,
	@SerializedName("nama_ibu_kandung")
	val namaIbuKandung: String? = null,
	@SerializedName("no_telp")
	val noTelp: String? = null,
	val plafon: Plafonprof? = null
)

data class Branchprof(
	@SerializedName("id_branch")
	val idBranch: String? = null,
	@SerializedName("longitude_branch")
	val longitudeBranch: Any? = null,
	@SerializedName("nama_branch")
	val namaBranch: String? = null,
	@SerializedName("latitude_branch")
	val latitudeBranch: Any? = null,
	@SerializedName("alamat_branch")
	val alamatBranch: String? = null
)

data class Usersprof(
	val password: String? = null,
	val role: Roleprof? = null,
	val nama: String? = null,
	@SerializedName("id_user")
	val idUser: String? = null,
	@SerializedName("is_active")
	val isActive: Boolean? = null,
	val email: String? = null
)

data class Plafonprof(
	@SerializedName("id_plafon")
	val idPlafon: String? = null,
	@SerializedName("jenis_plafon")
	val jenisPlafon: String? = null,
	@SerializedName("jumlah_plafon")
	val jumlahPlafon: Any? = null,
	val bunga: Any? = null
)

data class Roleprof(
	@SerializedName("id_role")
	val idRole: String? = null,
	@SerializedName("nama_role")
	val namaRole: String? = null
)
