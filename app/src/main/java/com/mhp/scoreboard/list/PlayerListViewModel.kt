package com.mhp.scoreboard.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerDao
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository

class PlayerListViewModel(application: Application) : AndroidViewModel(application) {

    val players : LiveData<List<Player>>

    private val repository: PlayerRepository

    init {
        val dao: PlayerDao = PlayerDatabase.getDatabase(application).playerDao()
        repository = PlayerRepository(dao)
        players = repository.allPlayers
    }

}
