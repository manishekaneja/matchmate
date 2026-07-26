package com.blahblah.matchmate.repository

import android.util.Log
import com.blahblah.matchmate.interfaces.ProfilesObserver
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.repository.base.BaseRepository
import com.blahblah.matchmate.repository.local.ProfileLocalDataSource
import com.blahblah.matchmate.repository.remote.ProfileService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ListProfilesRepository"
private const val SEED = "match_mate_random_seed"
private const val PAGE_SIZE = 10
private const val FIRST_PAGE = 1

class ListProfilesRepository(
    private val localDataSource: ProfileLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository<ProfileService>(ProfileService::class.java), ProfilesObserver {

    override fun observeProfiles() = localDataSource.observeProfiles()

    override suspend fun initialFetch(): Unit = withContext(dispatcher) {
        if (localDataSource.cachedCount() == 0) fetchPage(FIRST_PAGE)
    }

    override suspend fun paginationFetch(page: Int): Int = withContext(dispatcher) {
        fetchPage(page)
    }

    override suspend fun matchUsers(id: String, status: MatchStatus) =
        withContext(dispatcher) { localDataSource.updateStatus(id, status) }

    suspend fun syncPendingDecisions() = withContext(dispatcher) {
        val accepted = localDataSource.countByStatus(MatchStatus.ACCEPTED)
        val declined = localDataSource.countByStatus(MatchStatus.DECLINED)
        Log.d(TAG, "sync stub: $accepted accepted, $declined declined")
    }

    private suspend fun fetchPage(page: Int): Int {
        val results = api.getUsers(SEED, page = page, results = PAGE_SIZE).results.orEmpty()
        localDataSource.cache(results, System.currentTimeMillis())
        return results.size
    }
}
