package com.example.pinjemfinandroid.Activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.pinjemfinandroid.Model.DetailCustomerRequest
import com.example.pinjemfinandroid.Utils.LocationHelper
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.AccountViewModel
import com.example.pinjemfinandroid.ViewModel.DokumenViewModel
import com.example.pinjemfinandroid.databinding.ActivityAddDetailBinding


class AddDetailActivity : AppCompatActivity() {
    private lateinit var locationHelper: LocationHelper
    private lateinit var binding: ActivityAddDetailBinding
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var dokumenViewModel: DokumenViewModel
    var currentPhotoType: String? = null
    private var photoUri: Uri? = null


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            when (currentPhotoType) {
                "KTP" -> photoUri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"KTP")
                }
                "NPWP" -> photoUri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"NPWP")
                }
                "Selfie_KTP" -> photoUri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"Selfie_KTP")
                }

                "Kartu_Keluarga" -> photoUri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"Kartu_Keluarga")
                }

            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            when (currentPhotoType) {
                "KTP" -> uri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"KTP")
                }
                "NPWP" -> uri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"NPWP")
                }
                "Selfie_KTP" -> uri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"Selfie_KTP")
                }

                "Kartu_Keluarga" -> uri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it,"Kartu_Keluarga")
                }

            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationHelper = LocationHelper(this)
        preferenceHelper = PreferenceHelper(this)
        binding.buttonSubmit.backgroundTintList = null
        dokumenViewModel = ViewModelProvider(this).get(DokumenViewModel::class.java)
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

        binding.buttonUploadKk.setOnClickListener{
            currentPhotoType ="Kartu_Keluarga"
            requestPermissionsFile()
        }

        binding.buttonUploadKtp.setOnClickListener{
            currentPhotoType ="KTP"
            requestPermissionsFile()
        }

        binding.buttonUploadNpwp.setOnClickListener{
            currentPhotoType ="NPWP"
            requestPermissionsFile()
        }

        binding.buttonUploadSelfieKtp.setOnClickListener{
            currentPhotoType ="Selfie_KTP"
            requestPermissionsFile()
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

    fun openCamera() {

        photoUri = createImageUri() // Buat URI untuk menyimpan gambar
        cameraLauncher.launch(photoUri) // Luncurkan kamera untuk mengambil gambar
    }

    // Fungsi untuk memilih gambar dari galeri
    fun openGallery() {

        galleryLauncher.launch("image/*") // Luncurkan galeri untuk memilih gambar

    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }


    private fun uploadImage(uri: Uri,imagetype: String) {
        // Panggil fungsi uploadImage dari ViewModel
        val token = preferenceHelper.getString("token")

        if (token != null && token.isNotEmpty()) {
            // Token ada, lanjutkan upload gambar

            dokumenViewModel.uploadImage(this, uri, imagetype, token)
            dokumenViewModel.uploadResult.observe(this){
                when (imagetype) {
                    "KTP" -> {binding.imageViewKtp.setImageURI(uri)
                        binding.imageViewKtp.visibility = View.VISIBLE
                        binding.iconCheckKtp.visibility = View.VISIBLE}


                    "NPWP" -> {binding.imageViewNpwp.setImageURI(uri)
                        binding.imageViewNpwp.visibility = View.VISIBLE
                        binding.iconCheckNpwp.visibility = View.VISIBLE}

                    "Selfie_KTP" -> {binding.imageViewSelfieKtp.setImageURI(uri)
                        binding.imageViewSelfieKtp.visibility = View.VISIBLE
                        binding.iconCheckSelfieKtp.visibility = View.VISIBLE}

                    "Kartu_Keluarga" -> {binding.imageViewKk.setImageURI(uri)
                        binding.imageViewKk.visibility = View.VISIBLE
                        binding.iconCheckKk.visibility = View.VISIBLE}

                }

            }

        } else {
            // Token tidak ada, tampilkan alert untuk login
            android.app.AlertDialog.Builder(this)
                .setTitle("Anda belum login")
                .setMessage("Segera login untuk mengupload gambar.")
                .setPositiveButton("Login") { _, _ ->
                    // Arahkan ke halaman login
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                .setNegativeButton("Batal", null) // Tombol Cancel, tidak melakukan apa-apa
                .show()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Kamera", "Galeri")
        android.app.AlertDialog.Builder(this)
            .setTitle("Pilih sumber foto")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera() // Pilih kamera
                    1 -> openGallery() // Pilih galeri
                }
            }
            .show()
    }


    private fun requestPermissionsFile() {
        val permissions = mutableListOf<String>()

        // Memeriksa versi Android dan menambahkan permission yang sesuai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Untuk Android 13 (API 33) dan lebih tinggi
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES) // Akses gambar dari galeri
        } else {
            // Untuk versi Android sebelum API 33
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE) // Akses penyimpanan eksternal
        }

        permissions.add(Manifest.permission.CAMERA) // Akses kamera

        // Meluncurkan permission request

        permissionLauncher.launch(permissions.toTypedArray())
    }

    // Fungsi yang dipanggil saat meminta izin
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        if (cameraGranted && storageGranted) {
            // Jika izin kamera dan penyimpanan diberikan, tampilkan dialog memilih sumber gambar
            showImageSourceDialog()

        } else {
            // Jika izin tidak diberikan, tampilkan Toast
            Toast.makeText(this, "Izin kamera dan penyimpanan diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

}