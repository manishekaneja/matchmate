package com.blahblah.matchmate.ui.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.blahblah.matchmate.databinding.ProfileCardLayoutBinding
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.ui.recyclerview.interfaces.PaginationProvider
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.ProfileListingDiffingHandler
import com.blahblah.matchmate.ui.recyclerview.viewholder.ProfileCardViewHolder
class ProfileListingAdapter private constructor(
    private val interaction: ViewHolderInteractions,
    private val paginationProvider: PaginationProvider
) :
    ListAdapter<ProfileData, ProfileCardViewHolder>(
        ProfileListingDiffingHandler.instance
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCardViewHolder =
        ProfileCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).let {
            ProfileCardViewHolder(it, interaction)
        }

    override fun onBindViewHolder(holder: ProfileCardViewHolder, position: Int) =
        holder.bind(getItem(position)).also {
            if (paginationProvider.canPaginate() && (itemCount - position) <= paginationProvider.paginationThreshold()) {
                paginationProvider.paginationThreshold()
            }
        }

    fun getPayload(position: Int): ProfileData = getItem(position)


    companion object {
        fun getInstance(interaction: ViewHolderInteractions, paginationProvider: PaginationProvider): ProfileListingAdapter {
            return ProfileListingAdapter(interaction, paginationProvider)
        }
    }
}
