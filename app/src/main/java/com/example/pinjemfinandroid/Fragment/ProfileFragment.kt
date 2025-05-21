package com.example.pinjemfinandroid.Fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinjemfinandroid.Activity.AddDetailActivity
import com.example.pinjemfinandroid.Activity.LoginActivity
import com.example.pinjemfinandroid.Adapter.MenuAdapter
import com.example.pinjemfinandroid.Local.UserRoomViewModel
import com.example.pinjemfinandroid.Model.MenuModel
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.ConfirmationUtils.showConfirmationDialog
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.DokumenViewModel
import com.example.pinjemfinandroid.ViewModel.TokenNotifViewModel
import com.example.pinjemfinandroid.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),MenuAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var dataList: List<MenuModel>
    private val userRoomViewModel: UserRoomViewModel by viewModels()
    private var photoUri: Uri? = null
//    private lateinit var uploadImageViewModel: DokumenViewModel
    private val uploadImageViewModel: DokumenViewModel by activityViewModels()
    private val tokenNotifViewModel: TokenNotifViewModel by activityViewModels()
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                photoUri?.let {
                    // Upload foto yang diambil dengan kamera
                    uploadImage(it)
                }
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Upload foto yang dipilih dari galeri
                uploadImage(it)
            }
        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        preferenceHelper = PreferenceHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        ViewCompat.setZ(binding.profileImage, 2f)
        ViewCompat.setZ(binding.linearLayout, 1f)
        binding.btnLogoutAccount.backgroundTintList = null
        binding.btnLogoutAccount.setOnClickListener{

            showConfirmationDialog(
                context = requireContext(),  // Ganti 'this' dengan konteks sesuai (Activity)
                message = "Apakah Anda yakin ingin mengaktifkan fitur ini?",
                onConfirmed = {
                    // Aksi jika user memilih Yes

                },
                onCancelled = {
                    // Aksi jika user memilih No

                }
            )

        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataList = listOf(
            MenuModel("Profile Information", "Manage account details", R.drawable.baseline_account_circle_white),
            MenuModel("Plafon Information", "See Your Plafon here", R.drawable.baseline_credit_card_24),
            MenuModel("Payment History", "See your history paymeny", R.drawable.baseline_account_balance_wallet)
        )

        recyclerView = view.findViewById(R.id.recyclerMenu)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MenuAdapter(dataList,this)
        recyclerView.adapter = adapter

        binding.profileImage.setOnClickListener{
            requestPermissions()
        }

        preferenceHelper.getString("token")?.let { uploadImageViewModel.getProfileImage(it) }

        uploadImageViewModel.profileImageUrl.observe(viewLifecycleOwner, Observer { result ->
            Glide.with(requireContext())
                .load(result)
                .into(binding.profileImage)

        })

        uploadImageViewModel.profileImageError.observe(viewLifecycleOwner, Observer { result ->
            val user = FirebaseAuth.getInstance().currentUser

            // Cek apakah login pakai Google dan photoUrl tersedia
            val isGoogleUser = user?.providerData?.any { it.providerId == "google.com" } == true
            val photoUrl = user?.photoUrl

            if (isGoogleUser && photoUrl != null) {
                // Tampilkan foto Google ke ImageView
                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.profileImage)
            } else {
                // Kalau tidak ada apa-apa, bisa tampilkan placeholder atau biarkan kosong
                binding.profileImage.setImageResource(R.drawable.default_profile) // opsional
            }
        })
        observeUploadResult()

        binding.btnLogoutAccount.setOnClickListener {
            cleanTokenNotifFromAkun()

        }

        binding.tvEmail.text = preferenceHelper.getString("email").toString()
        binding.tvUsername.text = preferenceHelper.getString("username").toString()

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(), "Klik item $position", Toast.LENGTH_SHORT).show()
        when (position) {
            0 -> {
                startActivity(Intent(requireContext(),AddDetailActivity::class.java))
            }
            1 -> {

            }
            2 -> {

            }
            else -> {
                Toast.makeText(requireContext(), "Klik menu lainnya", Toast.LENGTH_SHORT).show()
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
        return requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }

    private fun uploadImage(uri: Uri) {
        // Panggil fungsi uploadImage dari ViewModel
        val token = preferenceHelper.getString("token")

        if (token != null && token.isNotEmpty()) {
            // Token ada, lanjutkan upload gambar
            showLoading(true)
            uploadImageViewModel.uploadImage(requireContext(), uri, "profile", token)
        } else {
            // Token tidak ada, tampilkan alert untuk login
            AlertDialog.Builder(requireContext())
                .setTitle("Anda belum login")
                .setMessage("Segera login untuk mengupload gambar.")
                .setPositiveButton("Login") { _, _ ->
                    // Arahkan ke halaman login
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
                .setNegativeButton("Batal", null) // Tombol Cancel, tidak melakukan apa-apa
                .show()
        }
    }

    private fun observeUploadResult() {
        uploadImageViewModel.uploadResult.observe(viewLifecycleOwner, Observer { result ->
            showLoading(false)
            Toast.makeText(requireContext(), "Upload Berhasil: $result", Toast.LENGTH_SHORT).show()
            val token = preferenceHelper.getString("token")
            token?.let { uploadImageViewModel.getProfileImage(it) }
        })

        uploadImageViewModel.uploadError.observe(viewLifecycleOwner, Observer { error ->
            showLoading(false)
            Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Kamera", "Galeri")
        AlertDialog.Builder(requireContext())
            .setTitle("Pilih sumber foto")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera() // Pilih kamera
                    1 -> openGallery() // Pilih galeri
                }
            }
            .show()
    }

    // Fungsi untuk meminta izin akses kamera dan penyimpanan
    private fun requestPermissions() {
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
            Toast.makeText(requireContext(), "Izin kamera dan penyimpanan diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE  // Menampilkan ProgressBar
            Log.d("ProfileFragment", "Progress bar tampil")
        } else {
            binding.progressBar.visibility = View.GONE  // Menyembunyikan ProgressBar
            Log.d("ProfileFragment", "Progress bar disembunyikan")
        }
    }


    private fun cleanTokenNotifFromAkun(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                preferenceHelper.getString("token")
                    ?.let { tokenNotifViewModel.postCleanTokenNotif(token, it) }

            }

        tokenNotifViewModel.cleanTokenResult.observe(viewLifecycleOwner){
            it.message?.let { it1 -> Log.d("Token Notifikasi", it1) }
            preferenceHelper.clear()
            userRoomViewModel.clearUsers()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }

        tokenNotifViewModel.cleanTokenError.observe(viewLifecycleOwner){
            it.let { it1 -> Log.d("Token Notifikasi", it1) }
        }
    }

}