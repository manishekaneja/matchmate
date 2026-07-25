package com.blahblah.matchmate.ui.screen.profilelisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.blahblah.matchmate.models.profile.ProfileData
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.repository.remote.ProfileService
import com.blahblah.matchmate.ui.screen.base.BaseViewModel

class ProfileListingViewModel(repository: ListProfilesRepository) :
    BaseViewModel<ProfileService>(ProfileListingViewModel::class.simpleName, repository) {

    var profiles: LiveData<List<ProfileData>> =
        repository.observeProfiles().asLiveData()

}

