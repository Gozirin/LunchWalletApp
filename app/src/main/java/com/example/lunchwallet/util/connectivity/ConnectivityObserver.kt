package com.example.lunchwallet.util.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityObserver  @Inject constructor(@ApplicationContext private val context: Context)
    : Connectivity {
//    override fun hasInternetAccess(): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork
//        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
//        return networkCapabilities != null &&
//                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//
//    }
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observeNetworkAccess(): Flow<Connectivity.NetworkStatus> {
        return callbackFlow {
            val callBack = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    launch { send(Connectivity.NetworkStatus.AVAILABLE) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(Connectivity.NetworkStatus.LOSING) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(Connectivity.NetworkStatus.LOST) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(Connectivity.NetworkStatus.UNAVAILABLE) }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callBack)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }

        }.distinctUntilChanged()
    }
}