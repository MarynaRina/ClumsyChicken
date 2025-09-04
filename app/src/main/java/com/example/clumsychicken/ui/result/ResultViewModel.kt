package com.example.clumsychicken.ui.result

import androidx.lifecycle.ViewModel
import com.example.clumsychicken.domain.usecase.ObserveHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    observeHigh: ObserveHighScoreUseCase
) : ViewModel() {
    val high = observeHigh()
}