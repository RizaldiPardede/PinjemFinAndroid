package com.example.pinjemfinandroid.Api
import com.example.pinjemfinandroid.Utils.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ApiConfig {

    private const val BASE_URL = "http://35.202.115.34/api/"
//
//    private val retrofit: Retrofit by lazy {
//        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//    }

    // Function to create OkHttpClient with or without Bearer Token
    private fun provideOkHttpClient(token: String? = null): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create OkHttpClient with Bearer Token if it's provided
        val clientBuilder = OkHttpClient.Builder().addInterceptor(loggingInterceptor)

        // Only add AuthInterceptor if token is not null
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


//    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
//    fun getPengajuanService(): PengajuanService = retrofit.create(PengajuanService::class.java)
//    fun getAkunService(): AkunService = retrofit.create(AkunService::class.java)

    // Services to interact with the API
    fun getApiService(token: String? = null): ApiService = provideRetrofit(token).create(ApiService::class.java)
    fun getPengajuanService(token: String? = null): PengajuanService = provideRetrofit(token).create(PengajuanService::class.java)
    fun getAkunService(token: String? = null): AkunService = provideRetrofit(token).create(AkunService::class.java)
}