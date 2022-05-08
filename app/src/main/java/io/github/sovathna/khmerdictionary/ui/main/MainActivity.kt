package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
      val shouldShowAppBar = destination.id != R.id.splash_fragment
      if (shouldShowAppBar) {
        supportActionBar?.show()
      } else {
        supportActionBar?.hide()
      }

      val shouldShowFab = destination.id == R.id.home_fragment
      if (shouldShowFab) {
        binding.appBarMain.fabSearch.show()
      } else {
        binding.appBarMain.fabSearch.hide()
      }
    }
    with(binding) {
      appBarMain.fabSearch.setOnClickListener {
        viewModel.updateSearchState()
      }
      appBarMain.etSearch.addTextChangedListener {
        viewModel.search(it?.trim()?.toString() ?: "")
      }
      drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {
          if (viewModel.current.isSearch) {
            viewModel.updateSearchState()
          }
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
    with(binding.appBarMain) {
      if (!state.isSearch) etSearch.text = null
      tilSearch.isVisible = state.isSearch
      val icon = if (state.isSearch) R.drawable.round_close_24 else R.drawable.round_search_24
      fabSearch.setIconResource(icon)
      if (state.isSearch) {
        ViewCompat.getWindowInsetsController(binding.root)?.let {
          val flags = WindowInsetsCompat.Type.ime()
          it.show(flags)
        }
        etSearch.requestFocus()
      } else {
        ViewCompat.getWindowInsetsController(binding.root)?.let {
          val flags = WindowInsetsCompat.Type.ime()
          it.hide(flags)
        }
        etSearch.clearFocus()
      }
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onBackPressed() {
    if (binding.drawerLayout.isOpen) {
      binding.drawerLayout.close()
    } else {
      super.onBackPressed()
    }
  }
}