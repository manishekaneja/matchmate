package com.blahblah.matchmate.ui.screen.profilelisting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.blahblah.matchmate.models.common.ProfilesData
import com.blahblah.matchmate.models.match.MatchStatus
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.repository.remote.ProfileService
import com.blahblah.matchmate.ui.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileListingViewModel(repository: ListProfilesRepository) :
    BaseViewModel<ProfileService, ProfilesData, ListProfilesRepository>(
        ProfileListingViewModel::class.simpleName,
        repository
    ) {
    val profiles: LiveData<List<ProfileData>>? = repository.observeProfiles().asLiveData()
    fun matchUsers(userId: String, isAccepted: Boolean) {
        viewModelScope.launch(loggingCoroutineCtx) {
            repository.matchUsers(
                userId,
                MatchStatus.ACCEPTED.takeIf { isAccepted } ?: MatchStatus.DECLINED
            )
        }
    }

    override fun onSuccess(result: ProfilesData) {
        viewModelScope.launch(loggingCoroutineCtx) {
            repository.dbManager.insertProfiles(result.profiles)
        }
    }

    override fun onPagination(result: ProfilesData) {
        viewModelScope.launch(loggingCoroutineCtx) {
            repository.dbManager.insertProfiles(result.profiles)
        }
    }

    override fun onFailure() {
        Log.e(tag, "something went wrong")
    }
}

