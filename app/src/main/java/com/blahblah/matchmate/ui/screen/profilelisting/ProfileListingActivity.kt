package com.blahblah.matchmate.ui.screen.profilelisting

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.blahblah.matchmate.databinding.ActivityMainBinding
import com.blahblah.matchmate.repository.ListProfilesRepository
import com.blahblah.matchmate.repository.local.AppDatabase
import com.blahblah.matchmate.ui.recyclerview.adapters.ProfileListingAdapter
import com.blahblah.matchmate.ui.recyclerview.interfaces.ViewHolderInteractions
import com.blahblah.matchmate.ui.recyclerview.utils.UserActionEnum
import com.blahblah.matchmate.ui.screen.base.BaseActivity


class ProfileListingActivity : BaseActivity<ActivityMainBinding>() {

    override val inflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    private val repository by lazy { ListProfilesRepository(dbManager = AppDatabase.dbManager) }

    private val viewmodel by lazy { ProfileListingViewModel(repository = repository) }

    private val profileListingAdapter: ProfileListingAdapter by lazy {
        ProfileListingAdapter.getInstance(viewHolderInteractions, viewmodel.paginationCallback)
    }

    private val viewHolderInteractions by lazy {
        object : ViewHolderInteractions {
            override fun onInteract(position: Int, action: UserActionEnum) {
                Log.e(viewmodel.tag, "$position $action")
                val payload = profileListingAdapter.getPayload(position)
                when (action) {
                    UserActionEnum.ProfileAccepted -> viewmodel.matchUsers(payload.identity, true)
                    UserActionEnum.ProfileRejected -> viewmodel.matchUsers(payload.identity, false)
                }
            }
        }
    }

    override fun initViews() {
        super.initViews()
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = profileListingAdapter
    }

    override fun initObservers() {
        Log.e(viewmodel.tag, "observer_setup_done")
        viewmodel.profiles?.observe(this) {
            Log.e(viewmodel.tag, it.toString())
            profileListingAdapter.submitList(it)
        }
    }
}