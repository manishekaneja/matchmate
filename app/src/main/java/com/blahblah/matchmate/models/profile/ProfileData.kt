package com.blahblah.matchmate.models.profile

import com.blahblah.matchmate.interfaces.RecyclerViewItem
import com.blahblah.matchmate.models.common.DobData
import com.blahblah.matchmate.models.common.LocationData
import com.blahblah.matchmate.models.common.NameData
import com.blahblah.matchmate.models.common.PictureData
import com.blahblah.matchmate.models.match.MatchStatus
import java.util.UUID

data class ProfileData(
    val login: LoginData,
    val name: NameData, val dob: DobData, val location: LocationData?, val picture: PictureData?,
    val status: MatchStatus
) : RecyclerViewItem {
    override fun isValid() = true
    override val identity: String
        get() = login.uuid ?: UUID.randomUUID().toString()

    data class LoginData(val uuid: String?)
}


