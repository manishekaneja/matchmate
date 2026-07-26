package com.blahblah.matchmate.ui.recyclerview.interfaces

import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum

interface ViewHolderInteractions {
    fun onInteract(profile: Profile, action: UserActionEnum)
}
