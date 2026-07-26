package com.blahblah.matchmate.ui.recyclerview.viewholder

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.matchmate.R
import com.blahblah.matchmate.databinding.ProfileCardLayoutBinding
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum
import com.bumptech.glide.Glide

class ProfileCardViewHolder(
    private val binding: ProfileCardLayoutBinding,
    private val interaction: ViewHolderInteractions
) : RecyclerView.ViewHolder(binding.root) {

    private var boundProfile: Profile? = null

    init {
        binding.acceptButton.setOnClickListener { notifyInteraction(UserActionEnum.ProfileAccepted) }
        binding.declineButton.setOnClickListener { notifyInteraction(UserActionEnum.ProfileRejected) }
    }

    fun bind(profile: Profile) {
        boundProfile = profile
        setName()
        setPhoto()
        setStatus()
    }

    private fun setName() {
        boundProfile?.also {
            binding.nameText.isVisible = true
            binding.metaText.isVisible = true
            binding.nameText.text =
                it.fullName.ifBlank { itemView.context.getString(R.string.profile_name_unknown) }
            binding.metaText.text = it.metaLine()
        } ?: run {
            binding.nameText.isVisible = false
            binding.metaText.isVisible = false
        }
    }

    private fun setPhoto() {
        boundProfile?.also {
            binding.photoImage.isVisible = true
            Glide.with(itemView)
                .load(it.pictureUrl.takeIf { it.isNotBlank() })
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .centerCrop()
                .into(binding.photoImage)
        } ?: run {
            binding.photoImage.isVisible = false
        }
    }

    private fun setStatus() {
        val decided = boundProfile?.status != MatchStatus.NONE
        binding.buttonGroup.isVisible = !decided
        binding.statusLabel.isVisible = decided

        if (decided) {
            val accepted = boundProfile?.status == MatchStatus.ACCEPTED
            binding.statusLabel.text = itemView.context.getString(
                if (accepted) R.string.member_accepted else R.string.member_declined
            )
            binding.statusLabel.backgroundTintList = ContextCompat.getColorStateList(
                itemView.context,
                if (accepted) R.color.accept_green else R.color.decline_red
            )
        }

    }

    private fun Profile.metaLine(): String {
        val context = itemView.context
        val place = listOf(city, state).filter { it.isNotBlank() }.joinToString(", ")
        val age = age.takeIf { it > 0 }
            ?.let { context.resources.getQuantityString(R.plurals.profile_age, it, it) }
        return listOfNotNull(age, place.takeIf { it.isNotBlank() }).joinToString(" · ")
            .ifBlank { context.getString(R.string.profile_meta_unknown) }
    }

    private fun notifyInteraction(action: UserActionEnum) {
        boundProfile?.let { interaction.onInteract(it, action) }
    }
}
