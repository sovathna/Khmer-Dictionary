package com.sovathna.androidmvi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.state.MviState
import com.sovathna.androidmvi.viewmodel.BaseViewModel
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import javax.inject.Inject

abstract class MviFragment<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>>(
    @LayoutRes private val layoutRes: Int
) : DaggerFragment() {
    @Inject
    protected lateinit var viewModel: VM

    abstract fun intents(): Observable<I>

    abstract fun render(state: S)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intents())
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer(this::render))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }
}