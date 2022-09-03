package com.example.lunchwallet.core.data // package com.decagon.aqua.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lunchwallet.models.UserX

@Database(entities = [UserX::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDAO
}
