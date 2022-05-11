package io.github.sovathna.khmerdictionary.ui.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : BaseViewModel<DetailState>(DetailState()) {
}