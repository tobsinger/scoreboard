package com.mhp.scoreboard.createplayer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerDao
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository
import com.mhp.scoreboard.score.ScoreViewModel
import kotlinx.coroutines.launch

class CreatePlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val tag = ScoreViewModel::class.java.simpleName
    private var dao: PlayerDao = PlayerDatabase.getDatabase(application).playerDao()
    private val repository: PlayerRepository =  PlayerRepository(dao)

    val name = MutableLiveData<String>()
    val closeDialog = MutableLiveData<Any>()

    fun submit() {
        Log.d(tag, "submit called")
        viewModelScope.launch { repository.insertPlayer(Player(name = name.value)) }
        closeDialog.postValue("Bye")
    }
}
