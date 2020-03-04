package com.mhp.scoreboard.createplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository
import kotlinx.coroutines.launch

class CreatePlayerViewModel(private val  repository: PlayerRepository) : ViewModel() {

    val name = MutableLiveData<String>()
    val closeDialog = MutableLiveData<Any>()

    fun submit() {
        viewModelScope.launch { repository.insertPlayer(Player(name = name.value)) }
        closeDialog.postValue("Bye")
    }
}
