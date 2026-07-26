package com.blahblah.matchmate.interfaces

import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfilesObserver {
    fun observeProfiles(): Flow<List<Profile>>
    suspend fun matchUsers(id: String, status: MatchStatus)
}
