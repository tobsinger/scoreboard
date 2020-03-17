package com.mhp.scoreboard.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.list.PlayerListRobot.Companion.withPlayerListRobot
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class PlayerListTest : KoinTest {

    private val items = listOf(
        Player(12, "foo", mutableListOf(1, 12, 3)),
        Player(911, "bar", mutableListOf(1, -12, 42, -99))
    )

    @Test
    fun testPlayerListItems() {
        withPlayerListRobot {
            setPlayers(items) {
                verifyListContainsCorrectNumberOfItems(items.size)
                verifyCheckContentForEachItem(items)
            }
        }
    }

    @Test
    fun testPlayerListNavigation() {
        withPlayerListRobot {
            setPlayers(items)
            clickOnRecyclerViewItem(1) {
                verifyTarget()
            }
        }
    }
}
