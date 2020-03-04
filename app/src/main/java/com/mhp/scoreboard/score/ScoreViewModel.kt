package com.mhp.scoreboard.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository
import kotlinx.coroutines.launch

class ScoreViewModel(private val repository: PlayerRepository, userId: Int) : ViewModel() {

    private val player: LiveData<Player> = repository.getPlayer(userId)

    val score = Transformations.map(player) { it?.actions?.sum()?.toString() }
    val actions = Transformations.map(player) { it?.actions }
    val pointsToChange = MutableLiveData<String>()
    val buttonsEnabled = Transformations.map(pointsToChange) { it?.isNotEmpty() }
    val back = MutableLiveData<Any>()
    val title = Transformations.map(player) { it?.name }

    /**
     * Add the entered value to the score of the current player
     */
    fun add() {
        val player = player.value!!
        pointsToChange.value?.toInt()?.let { player.actions.add(it) }
        persistAndNotify(player)
    }

    /**
     * Subtract the entered value from the score of the current player
     */
    fun subtract() {
        val player = player.value!!
        pointsToChange.value?.toInt()?.let { player.actions.add(it * -1) }
        persistAndNotify(player)
    }

    /**
     *  Persist the given instance of [Player] and trigger the navigate back functionality
     */
    private fun persistAndNotify(player: Player) {
        viewModelScope.launch {
            repository.update(player)
            back.postValue(true)
        }
    }
}
