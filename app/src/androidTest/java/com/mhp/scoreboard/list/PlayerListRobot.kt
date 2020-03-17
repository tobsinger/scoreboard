package com.mhp.scoreboard.list

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.mhp.scoreboard.BaseRobot
import com.mhp.scoreboard.R
import com.mhp.scoreboard.check
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class PlayerListRobot : BaseRobot() {

    private val players = MutableLiveData<List<Player>>()

    @RelaxedMockK
    private lateinit var repo: PlayerRepository

    override fun setupTestComponents() {
        every { repo.allPlayers } returns players
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            loadKoinModules(
                module {
                    viewModel {
                        PlayerListViewModel(repo)
                    }
                }
            )
        }
        setupFragmentScenario<PlayerListFragment>(null)
    }

    /**
     * Set the values of the list of players that should be displayed on the screen
     */
    fun setPlayers(playerValues: List<Player>, verification: (PlayerListRobotResult.() -> Unit)? = null): PlayerListRobotResult {
        players.postValue(playerValues)
        return PlayerListRobotResult().apply { verification?.let { it() } }
    }

    /**
     * Click on the item at a given position inside the recyclerview of the list of players
     */
    fun clickOnRecyclerViewItem(position: Int, verification: (PlayerListRobotResult.() -> Unit)?): PlayerListRobotResult {
        clickOnItemInRecyclerView(R.id.recyclerview, position)
        return PlayerListRobotResult().apply { verification?.let { it() } }
    }

    /**
     *  Assertions for test cases about [PlayerListRobot]
     */
    inner class PlayerListRobotResult {
        /**
         * Check that the next screen is the next screen would be the UserScoreFragment
         */
        fun verifyTarget() {
            assertEquals(
                navController.currentDestination!!.id,
                R.id.UserScoreFragment
            )
        }

        /**
         * Assert that the currently displayed number of list entries is equal to [numberOfItems]
         */
        fun verifyListContainsCorrectNumberOfItems(numberOfItems: Int) {
            checkRecyclerViewNumberOfEntries(R.id.recyclerview, numberOfItems)
        }

        /**
         * Assert that the content that is displayed inside the player list is displayed correctly
         */
        fun verifyCheckContentForEachItem(credits: List<Player>) {
            credits.forEachIndexed { index, item ->
                val name = item.name
                verifyNameValueAtPos(index, name!!)
                verifyScoreValueAtPos(index, item.actions.sum().toString())
            }
        }

        /**
         * Check the content of the name field at a given position inside the list of players
         */
        private fun verifyNameValueAtPos(pos: Int, expected: String) {
            checkTextInsideRecyclerView(
                R.id.recyclerview,
                pos,
                R.id.name,
                expected
            )
        }

        /**
         * Check the content of the score field at a given position inside the list of players
         */
        private fun verifyScoreValueAtPos(pos: Int, expected: String) {
            checkTextInsideRecyclerView(
                R.id.recyclerview,
                pos,
                R.id.score,
                expected
            )
        }
    }

    companion object {
        fun withPlayerListRobot(func: PlayerListRobot.() -> Unit) {
            PlayerListRobot().check(func)
        }
    }
}
