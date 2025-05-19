package com.example.pinjemfinandroid.ViewModel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjemfinandroid.Network.ApiConfig
import com.example.pinjemfinandroid.Model.GetProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DokumenViewModel:  ViewModel() {

    private val _uploadResult = MutableLiveData<Result<String>>()
    val uploadResult: LiveData<Result<String>> = _uploadResult

    private val _uploadError = MutableLiveData<String>()
    val uploadError: LiveData<String> = _uploadError

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> = _profileImageUrl

    fun uploadImage(context: Context, imageUri: Uri, imageType: String, token: String) {
        val realPath = getRealPathFromUri(context, imageUri)
        val file = File(realPath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val imageTypeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), imageType)

        val call = ApiConfig.uploadDokumenservice(token).uploadImage(imageTypeBody, multipartBody)

        call.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val message = response.body()?.get("message") ?: "Upload berhasil"
                    _uploadResult.value = Result.success(message)
                } else {
                    _uploadError.value = "Upload gagal: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                _uploadError.value = "Terjadi kesalahan: ${t.message}"
            }
        })

    }
    private fun getRealPathFromUri(context: Context, uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        throw IllegalArgumentException("Gagal mendapatkan path dari URI")

    }

    // Fungsi untuk mendapatkan gambar profil
    fun getProfileImage(token: String) {
        val call = ApiConfig.uploadDokumenservice(token).getProfileImage()

        call.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(call: Call<GetProfileResponse>, response: Response<GetProfileResponse>) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.imageProfile ?: ""
                    _profileImageUrl.value = imageUrl
                } else {
                    _uploadError.value = "Gagal mengambil gambar profil: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                _uploadError.value = "Terjadi kesalahan: ${t.message}"
                Log.d("Terjadi kesalahan", "${t.message}")
            }
        })
    }

}