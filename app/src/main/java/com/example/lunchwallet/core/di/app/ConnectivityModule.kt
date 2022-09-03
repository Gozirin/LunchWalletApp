package com.example.lunchwallet.core.di.app

import android.content.Context
import com.example.lunchwallet.util.connectivity.Connectivity
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConnectivityModule {

    @Provides
    @Singleton
    fun bindConnectivityChecker(@ApplicationContext context: Context): Connectivity{
        return ConnectivityObserver(context)
    }
}
