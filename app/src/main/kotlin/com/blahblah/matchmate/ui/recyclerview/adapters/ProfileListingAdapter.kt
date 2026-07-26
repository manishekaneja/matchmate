package com.blahblah.matchmate.ui.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.blahblah.matchmate.databinding.ProfileCardLayoutBinding
import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.ui.recyclerview.interfaces.PaginationProvider
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.ProfileListingDiffingHandler
import com.blahblah.matchmate.ui.recyclerview.viewholder.ProfileCardViewHolder

class ProfileListingAdapter(
    private val interaction: ViewHolderInteractions,
    private val paginationProvider: PaginationProvider
) : ListAdapter<Profile, ProfileCardViewHolder>(ProfileListingDiffingHandler) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProfileCardViewHolder(
        ProfileCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        interaction
    )

    override fun onBindViewHolder(holder: ProfileCardViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (itemCount - position <= paginationProvider.paginationThreshold()) {
            paginationProvider.performPaginationCall()
        }
    }
}
