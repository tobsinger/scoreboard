package com.mhp.scoreboard.createplayer

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mhp.scoreboard.createplayer.CreatePlayerRobot.Companion.withCreatePlayerRobot
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class CreatePlayerTest : KoinTest {

    @Test
    fun testCreatePlayer() {
        withCreatePlayerRobot {
            val name = "John Doe"
            enterName(name)
            clickOk {
                verifyNavigateToPlayerList()
                verifyPlayerCreated(name)
            }
        }
    }
}
