package com.harry.pay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String, // Must be 4 digits
    val businessName: String,
    val profilePictureUri: String // URI to profile picture
)


