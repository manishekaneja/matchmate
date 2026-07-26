package com.blahblah.matchmate.ui.screen.profilelisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.blahblah.matchmate.container.AppContainer
import com.blahblah.matchmate.interfaces.ConnectivityObserver
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.repository.remote.ProfileService
import com.blahblah.matchmate.ui.screen.base.BaseViewModel
import com.blahblah.matchmate.ui.screen.base.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

private const val TAG = "ProfileListingViewModel"
private const val LIVE_DATA_TIMEOUT = 5_000L

private data class ConnectionReadings(
    val previous: Boolean? = null,
    val current: Boolean? = null
) {
    fun next(online: Boolean) = ConnectionReadings(previous = current, current = online)

    val reconnected: Boolean get() = previous == false && current == true
}

class ProfileListingViewModel(
    repository: ListProfilesRepository,
    private val connectivityObserver: ConnectivityObserver
) : BaseViewModel<ProfileService, ListProfilesRepository>(TAG, repository) {

    private val online: SharedFlow<Boolean> = connectivityObserver.observe()
        .distinctUntilChanged()
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    private val reconnections: Flow<Unit> = online
        .runningFold(ConnectionReadings()) { readings, isOnline -> readings.next(isOnline) }
        .filter { it.reconnected }
        .map { }

    val isOffline: LiveData<Boolean> =
        online.map { !it }.asLiveData(timeoutInMs = LIVE_DATA_TIMEOUT)

    val eventStream: Flow<ProfileListingEvent> =
        reconnections.map { ProfileListingEvent.BackOnline }

    val uiState: LiveData<ProfileListingUiState> =
        combine(loadState, repository.observeProfiles()) { load, profiles ->
            when {
                profiles.isNotEmpty() -> ProfileListingUiState.Content(profiles)
                load is LoadState.Loading -> ProfileListingUiState.Loading
                load is LoadState.Failed -> ProfileListingUiState.Error(load.error)
                else -> ProfileListingUiState.Empty
            }
        }.onStart { emit(ProfileListingUiState.Loading) }
            .asLiveData(timeoutInMs = LIVE_DATA_TIMEOUT)

    init {
        observeConnectivity()
        loadFirstPage()
    }

    fun recordDecision(profileId: String, isAccepted: Boolean) {
        viewModelScope.launch {
            val status = if (isAccepted) MatchStatus.ACCEPTED else MatchStatus.DECLINED
            repository.matchUsers(profileId, status)
        }
    }

    fun retry() = loadFirstPage()

    private fun observeConnectivity() {
        viewModelScope.launch {
            reconnections.collect {
                repository.syncPendingDecisions()
                loadFirstPage()
            }
        }
    }

    companion object {
        fun factory() = viewModelFactory {
            initializer {
                ProfileListingViewModel(
                    repository = AppContainer.listProfilesRepository,
                    connectivityObserver = AppContainer.connectivityObserver
                )
            }
        }
    }
}
