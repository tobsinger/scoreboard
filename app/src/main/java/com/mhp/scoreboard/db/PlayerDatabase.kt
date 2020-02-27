package com.mhp.scoreboard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1)
abstract class PlayerDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PlayerDatabase? = null

        fun getDatabase(context: Context): PlayerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDatabase::class.java,
                    "player_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
