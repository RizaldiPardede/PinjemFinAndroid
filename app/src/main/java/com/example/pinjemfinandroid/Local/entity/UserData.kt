package com.example.pinjemfinandroid.Local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nama: String?,
    val email: String?,
    val nama_role: String?,
    val tempat_tgl_lahir: String?,
    val no_telp: String?,
    val alamat: String?,
    val nik: String?,
    val nama_ibu_kandung: String?,
    val pekerjaan: String?,
    val gaji: String?,
    val no_rek: String?,
    val status_rumah: String?,
    val jenis_plafon: String?,
    val jumlah_plafon: Double?,
    val bunga: Double?,
    val nama_branch: String?,
    val alamat_branch: String?,
    val latitude_branch: Double?,
    val longitude_branch: Double?,
    val sisa_plafon: Double?
)
