package io.github.sovathna.khmerdictionary.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

//    setSupportActionBar(binding.toolbar)
    val navController = findNavController(R.id.nav_host_fragment_content_main)
//    appBarConfiguration = AppBarConfiguration(setOf(R.id.MainFragment))
//    binding.toolbar.setupWithNavController(navController, appBarConfiguration)

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
      if (destination.id == R.id.MainFragment && navController.graph.startDestinationId == R.id.SplashFragment) {
        navController.graph.setStartDestination(R.id.MainFragment)
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

//  override fun onSupportNavigateUp(): Boolean {
//    val navController = findNavController(R.id.nav_host_fragment_content_main)
//    return navController.navigateUp(appBarConfiguration)
//        || super.onSupportNavigateUp()
//  }
}