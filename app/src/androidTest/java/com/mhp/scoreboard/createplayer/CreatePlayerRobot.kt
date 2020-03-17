package com.mhp.scoreboard.createplayer

import androidx.test.core.app.ApplicationProvider
import com.mhp.scoreboard.BaseRobot
import com.mhp.scoreboard.R
import com.mhp.scoreboard.check
import com.mhp.scoreboard.db.Player
import com.mhp.scoreboard.db.PlayerRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * Robot that contains the UI logic of the test cases for the [CreatePlayerDialogFragment]
 */
class CreatePlayerRobot : BaseRobot() {

    @RelaxedMockK
    lateinit var repo: PlayerRepository

    override fun setupTestComponents() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())

            loadKoinModules(
                module {
                    viewModel {
                        CreatePlayerViewModel(repo)
                    }
                }
            )
        }
        setupFragmentScenario<CreatePlayerDialogFragment>(null)
    }

    /**
     * Enter the name of the player that should be created into the input field
     */
    fun enterName(email: String) = fillEditText(R.id.input_name, email)

    /**
     * Click the ok button
     *
     * [verification] code that can be executed to verify the success of the operation
     */
    fun clickOk(verification: (CreatePlayerRobotResult.() -> Unit)? = null): CreatePlayerRobotResult {
        clickButton(R.id.button_submit)
        return CreatePlayerRobotResult().apply { verification?.let { it() } }
    }

    companion object {

        /**
         * Create an instance of the [CreatePlayerRobot] that opens the [CreatePlayerDialogFragment] and allows to prepare the state,
         * perform actions and verify the result
         */
        fun withCreatePlayerRobot(func: CreatePlayerRobot.() -> Unit = {}) {
            CreatePlayerRobot().check(func)
        }
    }

    /**
     *  Assertions for test cases about [CreatePlayerDialogFragment]
     */
    inner class CreatePlayerRobotResult {

        /**
         * Check if the navigation controller will open the [com.mhp.scoreboard.list.PlayerListFragment] next
         */
        fun verifyNavigateToPlayerList() {
            assertEquals(
                "The navigation target of the navController should be the player list fragment",
                navController.currentDestination!!.id,
                R.id.ListFragment
            )
        }

        /**
         * Check if a player got created with the given name
         */
        fun verifyPlayerCreated(name: String) {
            coVerify(exactly = 1) { repo.insertPlayer(Player(name = name)) }
        }
    }
}



