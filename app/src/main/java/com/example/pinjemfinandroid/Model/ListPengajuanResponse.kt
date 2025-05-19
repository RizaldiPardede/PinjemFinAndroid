data class ListPengajuanResponse(
	val pengajuanResponse: List<PengajuanResponseItem?>? = null
)

data class PengajuanResponseItem(
	val idUserCustomer: ResponseUserCustomer? = null,
	val amount: Double? = null,
	val tenor: Int? = null,
	val idPengajuan: String? = null,
	val angsuran: Double? = null,
	val bunga: Double? = null,
	val totalPayment: Double? = null,
	val status: String? = null
)

data class ResponseUserCustomer(
	val sisaPlafon: Double? = null,
	val noRek: String? = null,
	val gaji: String? = null,
	val tempatTglLahir: String? = null,
	val branch: ResponseBranch? = null,
	val users: ResponseUsers? = null,
	val alamat: String? = null,
	val statusRumah: String? = null,
	val nik: String? = null,
	val idUserCustomer: String? = null,
	val pekerjaan: String? = null,
	val namaIbuKandung: String? = null,
	val noTelp: String? = null,
	val plafon: ResponsePlafon? = null
)

data class ResponseUsers(
	val password: String? = null,
	val role: ResponseRole? = null,
	val nama: String? = null,
	val idUser: String? = null,
	val isActive: Boolean? = null,
	val email: String? = null
)

data class ResponseRole(
	val idRole: String? = null,
	val namaRole: String? = null
)

data class ResponsePlafon(
	val idPlafon: String? = null,
	val jenisPlafon: String? = null,
	val jumlahPlafon: Double? = null,
	val bunga: Double? = null
)

data class ResponseBranch(
	val idBranch: String? = null,
	val longitudeBranch: Double? = null,
	val namaBranch: String? = null,
	val latitudeBranch: Double? = null,
	val alamatBranch: String? = null
)
