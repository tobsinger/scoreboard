package com.mhp.scoreboard.score

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.mhp.scoreboard.R
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerDao
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application, userId: Int) : AndroidViewModel(application) {

    private val dao: PlayerDao = PlayerDatabase.getDatabase(application).playerDao()
    private val repository: PlayerRepository = PlayerRepository(dao)

    private val player: LiveData<Player> = repository.getPlayer(userId)

    val name = Transformations.map(player) { it.name }
    val score = Transformations.map(player) { it.score?.toString() }
    val pointsToChange = MutableLiveData<String>()
    val buttonsEnabled = Transformations.map(pointsToChange) { it.isNotEmpty() }
    val closeDialog = MutableLiveData<Any>()

    fun add() {
        val player = player.value!!
        player.score = player.score?.plus(pointsToChange.value?.toInt()!!)
        persistAndNotify(player)
    }

    fun subtract() {
        val player = player.value!!
        player.score = player.score?.minus(pointsToChange.value?.toInt()!!)
        persistAndNotify(player)
    }

    private fun persistAndNotify(player: Player) {
        viewModelScope.launch {
            repository.update(player)
            closeDialog.postValue(true)
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.notification_new_score, player.name, player.score),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
