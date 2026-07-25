package com.blahblah.matchmate.models.profile

import com.blahblah.matchmate.models.common.DobData
import com.blahblah.matchmate.models.common.LocationData
import com.blahblah.matchmate.models.common.NameData
import com.blahblah.matchmate.models.common.PictureData
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.repository.local.ProfileEntity
import java.util.UUID

data class ProfileData(
    val name: NameData?, val dob: DobData?, val location: LocationData?, val picture: PictureData?
)

