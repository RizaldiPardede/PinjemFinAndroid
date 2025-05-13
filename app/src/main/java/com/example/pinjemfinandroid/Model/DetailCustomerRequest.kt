package com.example.pinjemfinandroid.Model

data class DetailCustomerRequest(
	val status_rumah: String? = null,
	val nik: String? = null,
	var latitude_alamat: Double? = null,
	val pekerjaan: String? = null,
	val no_rek: String? = null,
	val nama_ibu_kandung: String? = null,
	val gaji: String? = null,
	val tempat_tgl_lahir: String? = null,
	val no_telp: String? = null,
	var longitude_alamat: Double? = null,
	val alamat: String? = null
)

