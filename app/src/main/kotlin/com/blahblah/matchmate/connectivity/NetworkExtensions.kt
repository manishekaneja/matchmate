package com.blahblah.matchmate.connectivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

private const val TAG = "ConnectivityManager"
fun ConnectivityManager?.isCurrentlyOnline(): Boolean =
    (this?.getNetworkCapabilities(activeNetwork)
        ?.hasInternet()
        ?: run {
            Log.e(TAG, "connectivityManager unavailable")
            true
        })

fun NetworkCapabilities?.hasInternet(): Boolean =
    this != null &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
