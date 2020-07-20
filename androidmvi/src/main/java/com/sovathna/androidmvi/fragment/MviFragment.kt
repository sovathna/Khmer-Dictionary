package com.sovathna.androidmvi.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.state.MviState
import com.sovathna.androidmvi.viewmodel.BaseViewModel
import io.reactivex.Observable

abstract class MviFragment
<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>>(
  @LayoutRes private val layoutRes: Int
) : Fragment(layoutRes) {

  protected abstract val viewModel: VM

  abstract fun intents(): Observable<I>

  abstract fun render(state: S)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.init(intents())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.stateLiveData.observe(viewLifecycleOwner, Observer(this::render))
  }

}