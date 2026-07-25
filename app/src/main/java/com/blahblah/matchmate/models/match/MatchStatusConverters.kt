package com.blahblah.matchmate.models.match

import androidx.room.TypeConverter

class MatchStatusConverters {
    @TypeConverter
    fun toStatus(v: String) = MatchStatus.valueOf(v)

    @TypeConverter
    fun fromStatus(s: MatchStatus) = s.name
}