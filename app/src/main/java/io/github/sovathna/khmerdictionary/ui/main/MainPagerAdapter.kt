package io.github.sovathna.khmerdictionary.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(
  fragment: Fragment,
  private val fragments: List<Fragment>
) : FragmentStateAdapter(fragment) {
  override fun getItemCount(): Int = fragments.size

  override fun createFragment(position: Int): Fragment = fragments[position]
}