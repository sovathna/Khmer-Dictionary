package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ActivityMainBinding
import io.github.sovathna.khmerdictionary.ui.viewBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)
  private val viewModel by viewModels<MainViewModel>()
  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    setSupportActionBar(binding.appBarMain.toolbar)
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    navController = navHostFragment.navController
    navController.graph.setStartDestination(R.id.home_fragment)
    appBarConfiguration =
      AppBarConfiguration(
        setOf(
          R.id.splash_fragment,
          R.id.home_fragment,
          R.id.nav_bookmark,
          R.id.nav_history,
          R.id.nav_settings,
          R.id.nav_about
        ), binding.drawerLayout
      )
    setupActionBarWithNavController(navController, appBarConfiguration)
    binding.navView.setupWithNavController(navController)
    navController.addOnDestinationChangedListener { nav, destination, args ->
      Timber.d("changed: ${destination.label}")
      val shouldShowAppBar = destination.id !in arrayOf(R.id.splash_fragment, R.id.nav_about)
      if (shouldShowAppBar) {
        supportActionBar?.show()
      } else {
        supportActionBar?.hide()
      }
      val isSingle = resources.getBoolean(R.bool.is_single)
      if (!isSingle && destination.id == R.id.nav_detail) {
        navController.popBackStack()
      }else {

      }
    }
    with(binding) {
      drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {

        }

        override fun onDrawerClosed(drawerView: View) {

        }

        override fun onDrawerStateChanged(newState: Int) {

        }
      })
    }
    viewModel.stateLiveData.observe(this, ::render)
  }

  private fun render(state: MainState) {

  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onBackPressed() {
    if (binding.drawerLayout.isOpen) {
      binding.drawerLayout.close()
    } else {
      if (navController.currentDestination?.id != R.id.home_fragment) {
        super.onBackPressed()
      } else {
        finishAfterTransition()
      }
    }
  }
}