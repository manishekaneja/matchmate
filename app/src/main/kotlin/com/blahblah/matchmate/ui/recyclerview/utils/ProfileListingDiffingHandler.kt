package com.blahblah.matchmate.ui.recyclerview.utils

import androidx.recyclerview.widget.DiffUtil
import com.blahblah.matchmate.models.profile.Profile

object ProfileListingDiffingHandler : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile) =
        oldItem.identity == newItem.identity

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile) = oldItem == newItem
}
