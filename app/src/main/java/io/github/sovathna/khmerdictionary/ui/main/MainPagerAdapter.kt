package io.github.sovathna.khmerdictionary.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(
  manager: FragmentManager,
  lifecycle: Lifecycle,
  private val fragments: List<Fragment>
) : FragmentStateAdapter(manager, lifecycle) {
  override fun getItemCount(): Int = fragments.size

  override fun createFragment(position: Int): Fragment = fragments[position]
}