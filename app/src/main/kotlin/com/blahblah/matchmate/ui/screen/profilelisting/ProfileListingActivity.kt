package com.blahblah.matchmate.ui.screen.profilelisting

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blahblah.matchmate.R
import com.blahblah.matchmate.databinding.ActivityMainBinding
import com.blahblah.matchmate.models.profile.Profile
import com.blahblah.matchmate.ui.recyclerview.adapters.PaginationLoaderAdapter
import com.blahblah.matchmate.ui.recyclerview.adapters.ProfileListingAdapter
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum
import com.blahblah.matchmate.ui.screen.base.BaseActivity
import kotlinx.coroutines.launch

class ProfileListingActivity : BaseActivity<ActivityMainBinding>(), ViewHolderInteractions {

    override val inflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    private val viewModel: ProfileListingViewModel by viewModels {
        ProfileListingViewModel.factory()
    }

    private val profileListingAdapter by lazy {
        ProfileListingAdapter(this, viewModel.paginationCallback)
    }

    private val paginationLoaderAdapter = PaginationLoaderAdapter()

    override fun onInteract(profile: Profile, action: UserActionEnum) {
        viewModel.recordDecision(
            profileId = profile.identity,
            isAccepted = action == UserActionEnum.ProfileAccepted
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ProfileListingActivity)
        recyclerView.adapter = ConcatAdapter(profileListingAdapter, paginationLoaderAdapter)
        retryButton.setOnClickListener { viewModel.retry() }
    }

    override fun initObservers() {
        viewModel.uiState.observe(this, ::render)
        viewModel.isOffline.observe(this, ::renderOfflineBanner)
        viewModel.isPaginating.observe(this) { loading ->
            binding.recyclerView.post { paginationLoaderAdapter.isLoading = loading }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventStream.collect(::handleEvent)
            }
        }
    }

    private fun render(state: ProfileListingUiState) = with(binding) {
        val showStatus =
            state is ProfileListingUiState.Empty || state is ProfileListingUiState.Error
        recyclerView.isVisible = state is ProfileListingUiState.Content
        progressBar.isVisible = state is ProfileListingUiState.Loading
        statusMessage.isVisible = showStatus
        retryButton.isVisible = showStatus

        when (state) {
            is ProfileListingUiState.Content -> profileListingAdapter.submitList(state.profiles)
            is ProfileListingUiState.Error -> statusMessage.setText(R.string.error_load_failed)
            ProfileListingUiState.Empty -> statusMessage.setText(R.string.empty_no_matches)
            ProfileListingUiState.Loading -> Unit
        }
    }

    private fun renderOfflineBanner(isOffline: Boolean) {
        binding.offlineBanner.isVisible = isOffline
    }

    private fun handleEvent(event: ProfileListingEvent) = when (event) {
        ProfileListingEvent.BackOnline ->
            Toast.makeText(this, R.string.back_online, Toast.LENGTH_SHORT).show()
    }
}
