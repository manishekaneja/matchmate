package com.blahblah.matchmate.ui.screen.profilelisting

sealed interface ProfileListingEvent {
    data object BackOnline : ProfileListingEvent
}