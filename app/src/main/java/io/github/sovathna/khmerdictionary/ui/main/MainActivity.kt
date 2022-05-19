package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ActivityMainBinding
import io.github.sovathna.khmerdictionary.extensions.showSnack
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)
  private val viewModel by viewModels<MainViewModel>()
  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration
  var drawerLayout:DrawerLayout? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    setSupportActionBar(binding.appBarMain.toolbar)
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    navController = navHostFragment.navController
    appBarConfiguration =
      AppBarConfiguration(
        setOf(
          R.id.nav_home,
          R.id.nav_bookmark,
          R.id.nav_history
        ), binding.drawerLayout
      )
    setupActionBarWithNavController(navController, appBarConfiguration)
    binding.navView.setupWithNavController(navController)
    navController.addOnDestinationChangedListener { _, destination, _ ->
      exitSnack?.dismiss()
      val shouldShowAppBar = destination.id !in arrayOf(R.id.splash_fragment, R.id.nav_about)
      if (shouldShowAppBar) {
        supportActionBar?.show()
      } else {
        supportActionBar?.hide()
      }
      val isSingle = resources.getBoolean(R.bool.is_single)
      if (!isSingle && destination.id == R.id.nav_detail) {
        navController.popBackStack()
      }
      if (destination.id in arrayOf(R.id.splash_fragment, R.id.nav_about)) {
        binding.appBarMain.contentMain.guide?.setGuidelinePercent(1F)
      } else {
        binding.appBarMain.contentMain.guide?.setGuidelinePercent(0.4F)
      }
      val shouldLockDrawer =
        destination.id in arrayOf(R.id.splash_fragment, R.id.nav_about, R.id.nav_settings)
      binding.drawerLayout.setDrawerLockMode(if (shouldLockDrawer) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    drawerLayout = binding.drawerLayout
    with(binding) {
      navView.setNavigationItemSelectedListener tmp@{
        drawerLayout.close()
        if (it.isChecked) return@tmp true
        val builder = NavOptions.Builder()
          .setEnterAnim(R.anim.from_right)
          .setExitAnim(R.anim.to_left)
          .setPopEnterAnim(R.anim.from_left)
          .setPopExitAnim(R.anim.to_right)
        when (it.itemId) {
          R.id.nav_home -> navController.navigateUp()
          R.id.nav_bookmark -> navController.navigate(
            R.id.nav_bookmark,
            null,
            builder
              .setPopUpTo(R.id.nav_home, false)
              .build()
          )
          R.id.nav_history -> navController.navigate(
            R.id.nav_history,
            null,
            builder
              .setPopUpTo(R.id.nav_home, false)
              .build()
          )
          R.id.nav_settings -> navController.navigate(
            R.id.nav_settings,
            null,
            builder.build()
          )
          R.id.nav_about -> navController.navigate(
            R.id.nav_about,
            null,
            builder.build()
          )
        }
        return@tmp true
      }
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  private var last = 0L

  private var exitSnack: Snackbar? = null

  override fun onBackPressed() {
    if (binding.drawerLayout.isOpen) {
      binding.drawerLayout.close()
    } else {
      if (navController.currentDestination?.id != R.id.nav_home) {
        super.onBackPressed()
      } else {
        val current = System.currentTimeMillis()
        if (last + 1500 > current) {
          finishAfterTransition()
        } else {
          last = current
          exitSnack = showSnack(
            parent = binding.root,
            message = getString(R.string.press_again_to_exit),
            duration = Snackbar.LENGTH_SHORT
          )
        }
      }
    }
  }
}