package com.harry.pay.data

import androidx.room.*
import com.harry.pay.model.PaymentLink

@Dao
interface PaymentLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(link: PaymentLink)

    @Query("SELECT * FROM payment_links ORDER BY createdAt DESC")
    suspend fun getAll(): List<PaymentLink>

    @Update
    suspend fun update(link: PaymentLink)

    @Delete
    suspend fun delete(link: PaymentLink)
}
