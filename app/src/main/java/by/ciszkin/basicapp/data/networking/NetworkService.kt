package by.ciszkin.basicapp.data.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkService {
    private const val APPLICATION_ID = "****************"
    private const val REST_API_KEY = "***************"
    private const val BASE_URL = "https://api.backendless.com/$APPLICATION_ID/$REST_API_KEY/"
    private const val KEY_NAME = "user-token"

    private lateinit var userToken: String

    private val backendlessHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader(KEY_NAME, userToken)
                .build()

            chain.proceed(request)
        }
        .build()

    fun getBackendlessApi(userToken: String) : BackendlessApi {
        this.userToken = userToken

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(backendlessHttpClient)
            .build()

        return retrofit.create()
    }

    fun getAnonymousBackendlessApi() : BackendlessApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create()
    }


}