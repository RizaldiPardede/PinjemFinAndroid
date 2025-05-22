package com.example.pinjemfinandroid.Model

data class ListPlafonResponse(
	val listPlafonResponse: List<ListPlafonResponseItem?>? = null
)

data class ListPlafonResponseItem(
	val id_plafon: String? = null,
	val jenis_plafon: String? = null,
	val jumlah_plafon: Double? = null,
	val bunga: Double? = null
)

