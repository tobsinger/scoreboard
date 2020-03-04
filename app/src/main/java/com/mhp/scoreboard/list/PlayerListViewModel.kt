package com.mhp.scoreboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository

class PlayerListViewModel(repository: PlayerRepository) : ViewModel() {

    val players: LiveData<List<Player>> = repository.allPlayers
}
