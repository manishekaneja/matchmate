package com.blahblah.matchmate.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.match.MatchStatusConverters
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.utils.toEntity
import com.blahblah.matchmate.utils.toProfileData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Database(entities = [ProfileEntity::class], version = 1)
@TypeConverters(MatchStatusConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        var instance: AppDatabase? = null
            private set

        lateinit var dbManager: AppDatabaseManager
            private set

        fun initialize(ctx: Context) = instance ?: synchronized(this) {
            val localInstance =
                Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "profiles")
                    .fallbackToDestructiveMigration()
                    .build()
            instance = localInstance
            dbManager = AppDatabaseManager(localInstance.profileDao())
        }

    }
}


class AppDatabaseManager(
    private val profileDao: ProfileDao,
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val dbDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getProfileObserver() = profileDao.observeAll()
        .map { profileList -> withContext(computationDispatcher) { profileList.map { it.toProfileData() } } }

    suspend fun insertProfiles(profiles: List<ProfileData>) =
        profileDao.insertAll(profiles.mapNotNull { it.toEntity(now = System.currentTimeMillis()) })

    suspend fun updateProfileStatus(profileId: String, status: MatchStatus) =
        profileDao.updateStatus(profileId, status)

}