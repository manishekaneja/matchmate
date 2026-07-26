package com.blahblah.matchmate.utils

import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.models.profile.ProfileDto
import com.blahblah.matchmate.repository.local.ProfileEntity

private const val UNKNOWN_AGE = 0

fun ProfileDto.toEntity(now: Long): ProfileEntity? {
    val id = login?.uuid?.takeIf { it.isNotBlank() } ?: return null
    return ProfileEntity(
        id = id,
        firstName = name?.first.orEmpty(),
        lastName = name?.last.orEmpty(),
        age = dob?.age ?: UNKNOWN_AGE,
        city = location?.city.orEmpty(),
        state = location?.state.orEmpty(),
        pictureUrl = picture?.large ?: picture?.medium.orEmpty(),
        status = MatchStatus.NONE,
        createdAt = now
    )
}

fun ProfileEntity.toProfile() = Profile(
    identity = id,
    fullName = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" "),
    age = age,
    city = city,
    state = state,
    pictureUrl = pictureUrl,
    status = status
)
