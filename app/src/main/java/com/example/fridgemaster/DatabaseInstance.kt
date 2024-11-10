package com.example.fridgemaster


import android.content.Context
import androidx.room.Room
import com.example.fridgemaster.AppDatabase

object DatabaseInstance {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "inventory"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}