package com.example.pinjemfinandroid.Activity

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.LocationHelper
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.AccountViewModel
import com.example.pinjemfinandroid.ViewModel.AuthViewModel
import com.example.pinjemfinandroid.databinding.ActivityAddDetailBinding


class AddDetailActivity : AppCompatActivity() {
    private lateinit var locationHelper: LocationHelper
    private lateinit var binding: ActivityAddDetailBinding
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationHelper = LocationHelper(this)
        preferenceHelper = PreferenceHelper(this)
        binding.buttonSubmit.backgroundTintList = null

        showLocationPermissionDialog()

        if (preferenceHelper.getString("token").isNullOrEmpty()){

            AlertDialog.Builder(this)
                .setTitle("Belum Login")
                .setMessage("Tolong lakukan login terlebih dahulu")
                .setPositiveButton("Iya") { _, _ ->
                    startActivity(Intent(this,LoginActivity::class.java))
                }
                .setNegativeButton("Tidak") { _, _ ->
                    startActivity(Intent(this,DashboardActivity::class.java))
                }
                .show()
        }
        else{
            binding.buttonSubmit.setOnClickListener {
                var tempatTglLahir = binding.editTextTempatTglLahir.text.toString()
                var noTelp = binding.editTextNoTelp.text.toString()
                var alamat = binding.editTextAlamat.text.toString()
                var nik = binding.editTextNik.text.toString()
                var namaIbuKandung = binding.editTextNamaIbuKandung.text.toString()
                var pekerjaan = binding.editTextPekerjaan.text.toString()
                var gaji = binding.editTextGaji.text.toString()
                var noRek = binding.editTextNoRek.text.toString()
                var statusRumah = binding.editTextStatusRumah.text.toString()

                if (latitude != null && longitude != null) {
                    val customerRequest = DetailCustomerRequest(statusRumah,nik,latitude,pekerjaan,noRek,namaIbuKandung,gaji,tempatTglLahir,noTelp,longitude,alamat)



                    // Kirim data ke backend
                    preferenceHelper.getString("token")
                        ?.let { it1 ->
                            accountViewModel.addDetailAccount(customerRequest, it1)
                            Toast.makeText(this, "Data Customer berhasil dikirim", Toast.LENGTH_SHORT).show()
                        }



                } else {
                    Toast.makeText(this, "Lokasi tidak valid", Toast.LENGTH_SHORT).show()
                }
            }

        }




    }

    private fun showLocationPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Ambil Lokasi Anda")
            .setMessage("Apakah Anda ingin mengizinkan aplikasi mengambil lokasi Anda?")
            .setPositiveButton("Iya") { _, _ ->
                if (locationHelper.hasLocationPermission()) {
                    ambilLokasiTerkini()
                } else {
                    locationHelper.requestLocationPermission(LocationHelper.LOCATION_PERMISSION_REQUEST_CODE)
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun ambilLokasiTerkini() {
        locationHelper.getLastLocation(
            onSuccess = { location: Location ->
                latitude = location.latitude
                longitude = location.longitude
                Toast.makeText(this, "Lokasi berhasil diambil", Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(this, "Gagal mengambil lokasi", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationHelper.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin lokasi diberikan", Toast.LENGTH_SHORT).show()
                ambilLokasiTerkini()
            } else {
                Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

}