package com.blahblah.matchmate.interfaces

import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfilesObserver {
    fun observeProfiles(): Flow<List<ProfileData>>
    suspend fun refresh()
    suspend fun updateStatus(id: String, status: MatchStatus)
}

