package by.ciszkin.basicapp.data.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkService {
    private const val APPLICATION_ID = "*******"
    private const val REST_API_KEY = "*******"
    private const val BASE_URL = "https://api.backendless.com/$APPLICATION_ID/$REST_API_KEY/"

    fun getBackendlessApi() : BackendlessApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create()
    }
}