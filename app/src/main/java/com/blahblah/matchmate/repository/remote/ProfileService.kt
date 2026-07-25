package com.blahblah.matchmate.repository.remote

import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.models.common.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileService : ApiService {
    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int = 10): UserResponseDto
}