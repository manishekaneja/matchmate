package com.blahblah.matchmate.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import com.blahblah.matchmate.interfaces.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "NetworkConnectivityObserver"

class NetworkConnectivityObserver(context: Context) : ConnectivityObserver {

    private val connectivityManager = context.applicationContext
        .getSystemService<ConnectivityManager>()

    override fun observe(): Flow<Boolean> = callbackFlow {
        val manager = connectivityManager
        if (manager == null) {
            Log.e(TAG, "connectivityManager unavailable")
            trySend(true)
            awaitClose { }
            return@callbackFlow
        }

        val usableNetworks = ConcurrentHashMap.newKeySet<Network>()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                if (capabilities.hasInternet()) {
                    usableNetworks.add(network)
                } else {
                    usableNetworks.remove(network)
                }
                trySend(usableNetworks.isNotEmpty())
            }

            override fun onLost(network: Network) {
                usableNetworks.remove(network)
                trySend(usableNetworks.isNotEmpty())
            }

            override fun onUnavailable() {
                trySend(usableNetworks.isNotEmpty())
            }
        }

        trySend(manager.isCurrentlyOnline())

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        manager.registerNetworkCallback(request, callback)

        awaitClose { manager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    override fun isOnline(): Boolean = connectivityManager.isCurrentlyOnline()
}
