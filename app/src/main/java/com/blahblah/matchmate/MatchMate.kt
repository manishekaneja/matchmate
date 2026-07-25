package com.blahblah.matchmate

import android.app.Application
import com.blahblah.matchmate.repository.local.AppDatabase

class MatchMateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.initialize(this)
    }
}