package com.blahblah.matchmate.models.common

import com.blahblah.matchmate.interfaces.Response
import com.blahblah.matchmate.models.profile.ProfileData

data class ProfilesData(val profiles: List<ProfileData>) : Response