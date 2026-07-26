package com.blahblah.matchmate.repository.local

import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.models.profile.ProfileDto
import com.blahblah.matchmate.utils.toEntity
import com.blahblah.matchmate.utils.toProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProfileLocalDataSource(
    private val profileDao: ProfileDao,
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    fun observeProfiles(): Flow<List<Profile>> = profileDao.observeAll()
        .map { rows -> withContext(computationDispatcher) { rows.map { it.toProfile() } } }

    suspend fun cachedCount(): Int = profileDao.count()

    suspend fun cache(profiles: List<ProfileDto>, now: Long): Int =
        profiles.mapNotNull { it.toEntity(now) }
            .also { profileDao.insertAll(it) }
            .size

    suspend fun updateStatus(profileId: String, status: MatchStatus) =
        profileDao.updateStatus(profileId, status)

    suspend fun countByStatus(status: MatchStatus): Int = profileDao.countByStatus(status)
}
