package com.mhp.scoreboard.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerDao
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application, userId: Int) : AndroidViewModel(application) {

    private val dao: PlayerDao = PlayerDatabase.getDatabase(application).playerDao()
    private val repository: PlayerRepository = PlayerRepository(dao)
    private val player: LiveData<Player> = repository.getPlayer(userId)

    val score = Transformations.map(player) { it?.actions?.sum()?.toString() }
    val actions = Transformations.map(player) { it?.actions }
    val pointsToChange = MutableLiveData<String>()
    val buttonsEnabled = Transformations.map(pointsToChange) { it?.isNotEmpty() }
    val back = MutableLiveData<Any>()
    val title = Transformations.map(player){it?.name}

    fun add() {
        val player = player.value!!
        pointsToChange.value?.toInt()?.let { player.actions.add(it) }
        persistAndNotify(player)
    }

    fun subtract() {
        val player = player.value!!
        pointsToChange.value?.toInt()?.let { player.actions.add(it * -1) }
        persistAndNotify(player)
    }

    private fun persistAndNotify(player: Player) {
        viewModelScope.launch {
            repository.update(player)
            back.postValue(true)
        }
    }
}
