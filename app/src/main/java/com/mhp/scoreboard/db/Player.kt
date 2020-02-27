package com.mhp.scoreboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
    var score: Int? = 0
)
