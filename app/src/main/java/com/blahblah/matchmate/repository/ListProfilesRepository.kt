package com.blahblah.matchmate.repository

import com.blahblah.matchmate.interfaces.ProfilesObserver
import com.blahblah.matchmate.models.common.ProfilesData
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.repository.base.BaseRepository
import com.blahblah.matchmate.repository.local.AppDatabaseManager
import com.blahblah.matchmate.repository.remote.ProfileService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val SEED = "match_mate_random_seed"
private const val THRESHOLD = 10

class ListProfilesRepository(
    val dbManager: AppDatabaseManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository<ProfileService, ProfilesData>(ProfileService::class.java),
    ProfilesObserver {

    override fun observeProfiles() = dbManager.getProfileObserver()

    override suspend fun initialFetch() = withContext(dispatcher) {
        val result =
            api.getUsers(SEED, page = 1, results = THRESHOLD).results?.takeIf { it.isNotEmpty() }
        ProfilesData(profiles = result ?: emptyList())
    }

    override suspend fun paginationFetch(page: Int) = withContext(dispatcher) {
        val result =
            api.getUsers(SEED, page, results = THRESHOLD).results?.takeIf { it.isNotEmpty() }
        ProfilesData(profiles = result ?: emptyList())
    }

    override suspend fun matchUsers(
        id: String,
        status: MatchStatus
    ) = dbManager.updateProfileStatus(id, status)
}