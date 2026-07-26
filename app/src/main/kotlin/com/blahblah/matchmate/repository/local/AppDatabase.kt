package com.blahblah.matchmate.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blahblah.matchmate.models.match.MatchStatusConverters

@Database(entities = [ProfileEntity::class], version = 1)
@TypeConverters(MatchStatusConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}
