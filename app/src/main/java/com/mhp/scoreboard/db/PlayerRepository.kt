package com.mhp.scoreboard.db

import androidx.lifecycle.LiveData

class PlayerRepository(private val playerDao: PlayerDao) {

    val allPlayers: LiveData<List<Player>> = playerDao.getAll()

    fun getPlayer(id: Int): LiveData<Player> {
        return playerDao.getById(id)
    }

    suspend fun update(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun insertPlayer(player: Player) {
        playerDao.insertPlayer(player)
    }

    suspend fun nukeTable() {
        playerDao.nukeTable()
    }
}
