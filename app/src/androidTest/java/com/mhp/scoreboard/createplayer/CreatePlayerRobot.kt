package com.mhp.scoreboard.createplayer

import com.mhp.scoreboard.BaseTestRobot
import com.mhp.scoreboard.R

class CreatePlayerRobot : BaseTestRobot() {
    fun setName(email: String) = fillEditText(R.id.input_name, email)
    fun clickOk() = clickButton(R.id.button_close)

//    lateinit var scenario: FragmentScenario<CreatePlayerDialogFragment>


    companion object {
        fun createPlayer(func: CreatePlayerRobot.() -> Unit) = CreatePlayerRobot()
            .apply {
                setupFragmentScenario<CreatePlayerDialogFragment>(null)
                func()
            }
    }
}
