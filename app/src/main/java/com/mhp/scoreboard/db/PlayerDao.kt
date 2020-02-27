package com.mhp.scoreboard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player")
    fun getAll(): LiveData<List<Player>>

    @Query("SELECT * FROM player WHERE id = :id")
    fun getById(id: Int): LiveData<Player>

    @Insert
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("DELETE FROM player")
    suspend fun nukeTable()
}
