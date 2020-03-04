package com.mhp.scoreboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.mhp.scoreboard.db.PlayerDao
import com.mhp.scoreboard.db.PlayerDatabase
import com.mhp.scoreboard.db.PlayerRepository
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // Get the NavController for your NavHostFragment
        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (findNavController(R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val dao: PlayerDao = PlayerDatabase.getDatabase(application).playerDao()
                val repository = PlayerRepository(dao)
                CoroutineScope(Dispatchers.Main).launch {
                    repository.nukeTable()
                    findNavController(R.id.nav_host_fragment).popBackStack(R.id.ListFragment, false)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
