package com.example.lunchwallet.core.data // package com.decagon.aqua.core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.lunchwallet.models.UserX

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: UserX)

    @Delete
    suspend fun deleteUser(user: UserX)
}
