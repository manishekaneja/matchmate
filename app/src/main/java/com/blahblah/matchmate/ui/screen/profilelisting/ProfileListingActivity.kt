package com.blahblah.matchmate.ui.screen.profilelisting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.blahblah.matchmate.databinding.ActivityMainBinding
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.ui.screen.base.BaseActivity


class ProfileListingActivity : BaseActivity<ActivityMainBinding>() {

    override val inflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    private val repository by lazy { ListProfilesRepository() }

    private val viewmodel by lazy { ProfileListingViewModel(repository = repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initObservers() {
        viewmodel.profiles.observe(this) {
            Log.e("PROFILE_DATA", it.toString())
        }
    }
}