package com.mhp.scoreboard.score

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.score.ScoreRobot.Companion.withScoreRobot
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class ScoreTest : KoinTest {

    private val playerId = 1
    private val player = Player(playerId, "John Doe", mutableListOf(12, 6, -9, 911))

    @Test
    fun testAddNewScore() {

        val score = 123
        withScoreRobot(playerId) {
            setPlayer(player) {
                verifyAddButtonDisabled()
                verifySubtractButtonDisabled()
                verifyCheckScoreContentForEachItemInRecyclerview(player.actions)
                verifyCurrentScore(player.actions.sum().toString())
            }
            enterNumber(score.toString()) {
                verifyAddButtonEnabled()
                verifySubtractButtonEnabled()
            }
            clickAdd {
                verifyValueAdded(score)
                verifyNavigateToListFragment()
            }
        }
    }

    @Test
    fun testSubtractNewScore() {
        val score = 321
        withScoreRobot(playerId) {
            setPlayer(player) {
                verifyCurrentScore(player.actions.sum().toString())
                verifyAddButtonDisabled()
                verifySubtractButtonDisabled()
            }
            enterNumber(score.toString()) {
                verifyAddButtonEnabled()
                verifySubtractButtonEnabled()
            }
            clickSubtract {
                verifyNavigateToListFragment()
                verifyValueAdded(-score)
            }
        }
    }
}
