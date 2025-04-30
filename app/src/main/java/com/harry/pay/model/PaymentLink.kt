package com.harry.pay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_links")
data class PaymentLink(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val amount: Double,
    val link: String,
    val createdAt: Long = System.currentTimeMillis()
)
