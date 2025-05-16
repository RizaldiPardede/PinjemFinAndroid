package com.example.pinjemfinandroid.Model

import com.google.gson.annotations.SerializedName

data class InformasiPengajuanResponse(
	@SerializedName("sisa_plafon")
	val sisaPlafon: Double? = null,

	@SerializedName("persentasilvup")
	val persentasilvup: Double? = null,

	@SerializedName("jumlah_pinjamanLunas")
	val jumlahPinjamanLunas: Double? = null,

	@SerializedName("jenis_plafon")
	val jenisPlafon: String? = null,

	@SerializedName("jumlah_pinjaman")
	val jumlahPinjaman: Double? = null,

	@SerializedName("jumlah_plafon")
	val jumlahPlafon: Double? = null
)

