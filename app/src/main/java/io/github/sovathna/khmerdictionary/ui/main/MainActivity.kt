package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)
  private val viewModel by viewModels<MainViewModel>()
  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    setupNavController()
    setupAppBarWithNavController()
    navController.addOnDestinationChangedListener { nav, destination, args ->
      val shouldShowAppBar = destination.id != R.id.splash_fragment
      if (shouldShowAppBar) {
        supportActionBar?.show()
      } else {
        supportActionBar?.hide()
      }
    }
    with(binding) {
      fabAdd.setOnClickListener {
        viewModel.updateSearchState()
      }
      etSearch.addTextChangedListener {
        viewModel.search(it?.trim()?.toString() ?: "")
      }
    }
    viewModel.stateLiveData.observe(this, ::render)
  }

  private fun render(state: MainState) {
    with(binding) {
      if (!state.isSearch) etSearch.text = null
      tilSearch.isVisible = state.isSearch
      val icon = if (state.isSearch) R.drawable.round_close_24 else R.drawable.round_search_24
      fabAdd.setIconResource(icon)
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

  private fun setupNavController() {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    navController = navHostFragment.navController
  }

  private fun setupAppBarWithNavController() {
    appBarConfiguration = AppBarConfiguration(setOf(R.id.splash_fragment, R.id.home_fragment))
    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}