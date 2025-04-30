package com.harry.pay.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harry.pay.model.User
import com.harry.pay.model.PaymentLink // Ensure you import PaymentLink

@Database(entities = [User::class, PaymentLink::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun paymentLinkDao(): PaymentLinkDao // Add the PaymentLinkDao method

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration() // DANGEROUS IN PRODUCTION, OK FOR NOW
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
