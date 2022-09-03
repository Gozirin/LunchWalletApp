package com.example.lunchwallet.core.di.app

import android.content.Context
import androidx.room.Room
import com.example.lunchwallet.core.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "lunch.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.getUserDao()
}

