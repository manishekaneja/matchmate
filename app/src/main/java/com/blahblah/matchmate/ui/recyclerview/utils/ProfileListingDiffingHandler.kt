package com.blahblah.matchmate.ui.recyclerview.utils

import androidx.recyclerview.widget.DiffUtil
import com.blahblah.matchmate.models.profile.ProfileData

class ProfileListingDiffingHandler private constructor() : DiffUtil.ItemCallback<ProfileData>() {
    override fun areItemsTheSame(a: ProfileData, b: ProfileData) = a.identity == b.identity

    override fun areContentsTheSame(a: ProfileData, b: ProfileData) = a == b

    companion object {
        private var _instance: ProfileListingDiffingHandler? = null
        val instance
            get() = _instance ?: initialize()

        fun initialize(): ProfileListingDiffingHandler {
            return ProfileListingDiffingHandler().also {
                _instance = it
            }
        }

    }
}