package com.blahblah.matchmate

import android.app.Application
import com.blahblah.matchmate.container.AppContainer

class MatchMateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}
