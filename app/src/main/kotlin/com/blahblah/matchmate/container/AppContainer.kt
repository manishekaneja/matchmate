package com.blahblah.matchmate.container

import android.content.Context
import androidx.room.Room
import com.blahblah.matchmate.connectivity.NetworkConnectivityObserver
import com.blahblah.matchmate.interfaces.ConnectivityObserver
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.repository.local.AppDatabase
import com.blahblah.matchmate.repository.local.ProfileLocalDataSource

private const val DATABASE_NAME = "profiles"

object AppContainer {

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    private val profileLocalDataSource by lazy { ProfileLocalDataSource(database.profileDao()) }

    val connectivityObserver: ConnectivityObserver by lazy {
        NetworkConnectivityObserver(appContext)
    }

    val listProfilesRepository by lazy { ListProfilesRepository(profileLocalDataSource) }
}
