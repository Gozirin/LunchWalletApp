package com.example.lunchwallet.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "user"
)
data class UserX(
    @PrimaryKey
    val name: String,
    val email: String
) : Serializable
