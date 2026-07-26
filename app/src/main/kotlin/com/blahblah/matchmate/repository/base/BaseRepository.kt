package com.blahblah.matchmate.repository.base

import com.blahblah.matchmate.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseRepository<S : ApiService>(clazz: Class<S>) {

    protected val api: S = retrofit.create(clazz)

    abstract suspend fun initialFetch()

    abstract suspend fun paginationFetch(page: Int): Int

    companion object {
        private const val ENDPOINT = "https://randomuser.me/"

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
