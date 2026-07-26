package com.blahblah.matchmate.repository.remote

import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.models.profile.ProfilesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileService : ApiService {
    @GET("api/")
    suspend fun getUsers(
        @Query("seed") seed: String,
        @Query("page") page: Int,
        @Query("results") results: Int
    ): ProfilesResponseDto
}
