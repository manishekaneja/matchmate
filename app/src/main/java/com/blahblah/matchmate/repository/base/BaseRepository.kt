package com.blahblah.matchmate.repository.base

import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.repository.local.AppDatabaseManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseRepository<S : ApiService>(
    clazz: Class<S>,
    protected val dbManager: AppDatabaseManager,
) {

    val api: S = retrofit.create(clazz)

    abstract suspend fun initialFetch()


    companion object {

        private val logging =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        private val client = OkHttpClient.Builder().addInterceptor(logging).build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private const val ENDPOINT = "https://randomuser.me/"
    }
}