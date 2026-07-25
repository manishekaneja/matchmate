package com.blahblah.matchmate.models.match

import com.blahblah.matchmate.models.common.DobData
import com.blahblah.matchmate.models.common.LocationData
import com.blahblah.matchmate.models.common.NameData
import com.blahblah.matchmate.models.common.PictureData

data class MatchData(
    val id: String,
    val fullName: NameData,
    val age: DobData,
    val location: LocationData,
    val picture: PictureData,
    val status: MatchStatus
)