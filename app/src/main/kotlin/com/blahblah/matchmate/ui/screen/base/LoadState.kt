package com.blahblah.matchmate.ui.screen.base

sealed interface LoadState {
    data object Loading : LoadState
    data object Loaded : LoadState
    data class Failed(val error: Throwable) : LoadState
}
