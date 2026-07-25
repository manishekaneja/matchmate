package com.blahblah.matchmate.utils

import com.blahblah.matchmate.models.common.DobData
import com.blahblah.matchmate.models.common.LocationData
import com.blahblah.matchmate.models.common.NameData
import com.blahblah.matchmate.models.common.PictureData
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.repository.local.ProfileEntity

fun ProfileData.toEntity(now: Long) =
    if (login.uuid != null && location != null && picture != null) {
        ProfileEntity(
            id = login.uuid,
            firstName = name.first,
            lastName = name.last,
            age = dob.age,
            city = location.city ?: "",
            state = location.state ?: "",
            pictureUrl = picture.large,
            status = MatchStatus.NONE,
            createdAt = now,
        )
    } else {
        null
    }


fun ProfileEntity.toProfileData(): ProfileData = ProfileData(
    name = NameData(first = firstName, last = lastName),
    location = LocationData(city, state),
    picture = PictureData(pictureUrl, pictureUrl),
    dob = DobData(age),
    login = ProfileData.LoginData(id),
    status = status,
)