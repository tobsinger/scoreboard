package com.mhp.scoreboard.dependencyinjection

import com.mhp.scoreboard.createplayer.CreatePlayerViewModel
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository
import com.mhp.scoreboard.list.PlayerListViewModel
import com.mhp.scoreboard.score.ScoreViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scoreBoardModule = module {

    factory {
        PlayerRepository(get())
    }

    factory {
        PlayerDatabase.getDatabase(get()).playerDao()
    }

    viewModel {
        CreatePlayerViewModel(get())
    }

    viewModel {
        PlayerListViewModel(get())
    }

    viewModel { (userId: Int) ->
        ScoreViewModel(get(), userId)
    }
}
