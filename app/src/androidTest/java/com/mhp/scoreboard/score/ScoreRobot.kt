package com.mhp.scoreboard.score

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.mhp.scoreboard.BaseRobot
import com.mhp.scoreboard.R
import com.mhp.scoreboard.check
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * Robot to perform the
 *
 * [playerId] The ID of the player to be used to create an instance of the [ScoreViewModel]
 */
class ScoreRobot(
    private val playerId: Int
) : BaseRobot() {

    @RelaxedMockK
    lateinit var repo: PlayerRepository

    private val player = MutableLiveData<Player>()

    override fun setupTestComponents() {
        every { repo.getPlayer(playerId) } returns player
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            loadKoinModules(
                module {
                    viewModel {
                        ScoreViewModel(repo, playerId)
                    }
                }
            )
        }
        setupFragmentScenario<ScoreFragment>(bundleOf(Pair("id", playerId)))
    }

    fun setPlayer(player: Player, validation: (ScoreRobotRobotResult.() -> Unit)? = null): ScoreRobotRobotResult {
        this.player.postValue(player)
        return ScoreRobotRobotResult().apply { validation?.let { it() } }
    }

    /**
     * Click the "add" button
     */
    fun clickAdd(func: (ScoreRobotRobotResult.() -> Unit)?): ScoreRobotRobotResult {
        clickButton(R.id.button_add)
        return ScoreRobotRobotResult().apply { func?.let { it() } }
    }

    /**
     * Click the "subtract" button
     */
    fun clickSubtract(func: (ScoreRobotRobotResult.() -> Unit)?): ScoreRobotRobotResult {
        clickButton(R.id.button_subtract)
        return ScoreRobotRobotResult().apply { func?.let { it() } }
    }

    /**
     * Enter a value for the new score of the current player
     */
    fun enterNumber(number: String, verification: (ScoreRobotRobotResult.() -> Unit)? = null): ScoreRobotRobotResult {
        fillEditText(R.id.input_score, number)
        return ScoreRobotRobotResult().apply { verification?.let { it() } }
    }

    /**
     * The verification logic for the [ScoreRobot]
     */
    inner class ScoreRobotRobotResult {
        fun verifyNavigateToListFragment() {
            Assert.assertEquals(
                navController.currentDestination!!.id,
                R.id.ListFragment
            )
        }

        /**
         * Verifies that the given value will be added to the score of the user and persisted in the DB
         */
        fun verifyValueAdded(i: Int) {
            val currentPlayer = player.value!!
            Assert.assertEquals("The last item should be the new value", i, currentPlayer.actions.last())
            coVerify(exactly = 1) { repo.update(currentPlayer) }
        }

        /**
         * Check if the subtract button is enabled
         */
        fun verifySubtractButtonEnabled() {
            checkButtonEnabled(R.id.button_subtract)
        }

        /**
         * Check if the subtract button is disabled
         */
        fun verifySubtractButtonDisabled() {
            checkButtonDisabled(R.id.button_subtract)
        }

        /**
         * Check if the add button is enabled
         */
        fun verifyAddButtonEnabled() {
            checkButtonEnabled(R.id.button_add)
        }

        /**
         * Check if the add button is disabled
         */
        fun verifyAddButtonDisabled() {
            checkButtonDisabled(R.id.button_add)
        }

        /**
         * Verifies the displayed values inside the action history of the current player
         */
        fun verifyCheckScoreContentForEachItemInRecyclerview(credits: List<Int>) {
            credits.forEachIndexed { index, item ->
                verifyScoreValueAtPos(index, item.toString())
            }
        }

        /**
         * Verifies the value of the current user score
         */
        fun verifyCurrentScore(score: String) {
            matchText(R.id.textview_current_score, score)
        }

        /**
         * Verifies one entry of the displayed values inside the action history of the current player
         */
        private fun verifyScoreValueAtPos(pos: Int, expected: String) {
            checkTextInsideRecyclerView(
                R.id.actions,
                pos,
                R.id.score,
                expected
            )
            checkTextInsideRecyclerView(
                R.id.actions,
                pos,
                R.id.roundNumber,
                (pos + 1).toString()
            )
        }
    }

    companion object {
        fun withScoreRobot(playerId: Int, func: ScoreRobot.() -> Unit = {}) {
            ScoreRobot(playerId).check(func)
        }
    }
}
