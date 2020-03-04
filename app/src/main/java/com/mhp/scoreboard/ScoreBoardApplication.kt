package com.mhp.scoreboard

import android.app.Application
import com.mhp.scoreboard.dependencyinjection.scoreBoardModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScoreBoardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ScoreBoardApplication)
            modules(scoreBoardModule)
        }
    }
}
