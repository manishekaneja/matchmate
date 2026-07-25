package com.blahblah.matchmate.ui.recyclerview.interfaces

import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum

interface ViewHolderInteractions {
    fun onInteract(position: Int, action: UserActionEnum)
}
