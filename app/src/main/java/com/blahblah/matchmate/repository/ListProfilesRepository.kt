package com.blahblah.matchmate.repository

import com.blahblah.matchmate.interfaces.ProfilesObserver
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.repository.base.BaseRepository
import com.blahblah.matchmate.repository.local.AppDatabase
import com.blahblah.matchmate.repository.remote.ProfileService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListProfilesRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository<ProfileService>(ProfileService::class.java, AppDatabase.dbManager),
    ProfilesObserver {

    override fun observeProfiles() = dbManager.getProfileObserver()

    override suspend fun initialFetch() {
        refresh()
    }


    override suspend fun refresh(): Unit = withContext(dispatcher) {
        api.getUsers().results?.takeIf { it.isNotEmpty() }?.let {
            dbManager.insertProfiles(it)
        }
    }

    override suspend fun updateStatus(
        id: String,
        status: MatchStatus
    ) {

    }
}