package com.blahblah.matchmate.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blahblah.matchmate.models.common.DobData
import com.blahblah.matchmate.models.common.LocationData
import com.blahblah.matchmate.models.common.NameData
import com.blahblah.matchmate.models.common.PictureData
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.ProfileData

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
    val state: String,
    val pictureUrl: String,
    val status: MatchStatus = MatchStatus.NONE,
    val createdAt: Long
)