package com.sovathna.androidmvi.viewmodel

import androidx.lifecycle.ViewModel
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.androidmvi.state.MviState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

abstract class MviViewModel<I : MviIntent, R : MviResult, S : MviState> :
    ViewModel(),
    BaseViewModel<I, S> {

    protected val disposables = CompositeDisposable()
    protected val intents = PublishSubject.create<I>()

    override fun init(intents: Observable<I>) {
        intents.subscribe(this.intents)
    }

    protected abstract val reducer: BiFunction<S, R, S>

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}