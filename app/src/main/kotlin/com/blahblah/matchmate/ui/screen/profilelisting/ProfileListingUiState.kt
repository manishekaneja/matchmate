package com.blahblah.matchmate.ui.screen.profilelisting

import com.blahblah.matchmate.models.profile.Profile

sealed interface ProfileListingUiState {
    data object Loading : ProfileListingUiState
    data class Content(val profiles: List<Profile>) : ProfileListingUiState
    data object Empty : ProfileListingUiState
    data class Error(val error: Throwable) : ProfileListingUiState
}
