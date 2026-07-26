package com.blahblah.matchmate.models.profile

import com.blahblah.matchmate.interfaces.RecyclerViewItem
import com.blahblah.matchmate.models.match.MatchStatus

data class Profile(
    override val identity: String,
    val fullName: String,
    val age: Int,
    val city: String,
    val state: String,
    val pictureUrl: String,
    val status: MatchStatus
) : RecyclerViewItem
