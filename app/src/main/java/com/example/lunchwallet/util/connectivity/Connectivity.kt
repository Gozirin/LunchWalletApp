package com.example.lunchwallet.util.connectivity

import kotlinx.coroutines.flow.Flow

interface Connectivity {
    fun observeNetworkAccess(): Flow<NetworkStatus>

    sealed class NetworkStatus {
        object AVAILABLE: NetworkStatus()
        object UNAVAILABLE: NetworkStatus()
        object LOSING: NetworkStatus()
        object LOST: NetworkStatus()
    }
}