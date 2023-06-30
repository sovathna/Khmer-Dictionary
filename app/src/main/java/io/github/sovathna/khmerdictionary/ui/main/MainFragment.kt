package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.crazylegend.viewbinding.viewBinding
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}