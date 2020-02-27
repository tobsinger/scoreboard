package com.mhp.scoreboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "player")
@TypeConverters(GenreConverter::class)
data class Player(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
    var actions: MutableList<Int> = arrayListOf()
)

class GenreConverter {
    @TypeConverter
    fun gettingListFromString(genreIds: String): List<Int> {
        val list: MutableList<Int> = ArrayList()
        val array = genreIds.split(",").toTypedArray()
        for (s in array) {
            if (s.isNotEmpty()) {
                list.add(s.toInt())
            }
        }
        return list
    }

    @TypeConverter
    fun writingStringFromList(list: List<Int>): String {
        var genreIds = ""
        for (i in list) {
            genreIds += ",$i"
        }
        return genreIds
    }
}


