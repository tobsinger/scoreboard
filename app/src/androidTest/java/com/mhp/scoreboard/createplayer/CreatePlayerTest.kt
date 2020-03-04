package com.mhp.scoreboard.createplayer

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.mhp.scoreboard.createplayer.CreatePlayerRobot.Companion.createPlayer
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CreatePlayerTest {

    @Test
    fun testCreatePlayer() {
        createPlayer {
            setName("foo")
            clickOk()
        }
    }
}
