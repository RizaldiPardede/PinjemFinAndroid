package com.example.pinjemfinandroid.Network
import com.example.pinjemfinandroid.Utils.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ApiConfig {

    private const val BASE_URL = "http://34.30.17.63/api/"
//private const val BASE_URL = "https://417c-120-188-36-27.ngrok-free.app/api/"
    private fun provideOkHttpClient(token: String? = null): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
        token?.let {
            clientBuilder.addInterceptor(AuthInterceptor(it))
        }

        return clientBuilder.build()
    }

    // Retrofit instance with optional Bearer Token
    private fun provideRetrofit(token: String? = null): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(token))  // Use OkHttpClient with optional token
            .build()
    }



    // Services to interact with the API
    fun getApiService(token: String? = null): ApiService = provideRetrofit(token).create(ApiService::class.java)
    fun getPengajuanService(token: String? = null): PengajuanService = provideRetrofit(token).create(PengajuanService::class.java)
    fun getAkunService(token: String? = null): AkunService = provideRetrofit(token).create(AkunService::class.java)

    fun uploadDokumenservice(token: String? = null): DokumenService = provideRetrofit(token).create(DokumenService::class.java)
}