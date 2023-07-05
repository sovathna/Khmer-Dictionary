package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentMainBinding
import io.github.sovathna.khmerdictionary.ui.settings.SettingsFragment
import io.github.sovathna.khmerdictionary.ui.words.bookmark.BookmarksFragment
import io.github.sovathna.khmerdictionary.ui.words.history.HistoriesFragment
import io.github.sovathna.khmerdictionary.ui.words.home.WordsFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private val binding by viewBinding(FragmentMainBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fragments = listOf(
      WordsFragment(),
      HistoriesFragment(),
      BookmarksFragment(),
      SettingsFragment()
    )
    val adapter = MainPagerAdapter(this, fragments)

    val menuIds =
      listOf(
        R.id.action_home,
        R.id.action_histories,
        R.id.action_bookmarks,
        R.id.action_settings
      )
    with(binding) {
      viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
          navView.selectedItemId = menuIds[position]
        }
      })
      navView.setOnItemSelectedListener {
        viewPager.currentItem = menuIds.indexOf(it.itemId)
        true
      }
      viewPager.adapter = adapter
    }
  }

}