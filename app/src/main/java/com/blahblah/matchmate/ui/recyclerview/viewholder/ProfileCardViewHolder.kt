package com.blahblah.matchmate.ui.recyclerview.viewholder

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.matchmate.R
import com.blahblah.matchmate.databinding.ProfileCardLayoutBinding
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum
import com.bumptech.glide.Glide

class ProfileCardViewHolder(
    private val binding: ProfileCardLayoutBinding, private val interaction: ViewHolderInteractions
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.acceptButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) interaction.onInteract(
                position, UserActionEnum.ProfileAccepted
            )
        }
        binding.declineButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) interaction.onInteract(
                position, UserActionEnum.ProfileRejected
            )
        }
    }

    fun bind(item: ProfileData) = with(binding) {
        nameText.text = item.name.fullname
        metaText.text = itemView.context.getString(
            R.string.profile_meta, item.dob.age, item.location?.city, item.location?.state
        )

        Glide.with(itemView).load(item.picture?.large).centerCrop().into(photoImage)

        buttonGroup.isVisible = item.status == MatchStatus.NONE
        statusButton.isVisible =
            item.status == MatchStatus.ACCEPTED || item.status == MatchStatus.DECLINED

        val accepted = item.status == MatchStatus.ACCEPTED
        statusButton.text = itemView.context.getString(
            if (accepted) R.string.member_accepted else R.string.member_declined
        )
        statusButton.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context, if (accepted) R.color.accept_green else R.color.decline_red
            )
        )

    }
}