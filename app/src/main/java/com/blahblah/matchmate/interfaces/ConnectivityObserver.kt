package com.blahblah.matchmate.interfaces

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Boolean>
    fun isOnline(): Boolean
}