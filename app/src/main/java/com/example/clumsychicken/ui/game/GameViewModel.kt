package com.example.clumsychicken.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clumsychicken.core.navigation.GameMode
import com.example.clumsychicken.domain.model.FallingEgg
import com.example.clumsychicken.domain.model.GameConfig
import com.example.clumsychicken.domain.usecase.ProcessTapUseCase
import com.example.clumsychicken.domain.usecase.SpawnEggUseCase
import com.example.clumsychicken.domain.usecase.StartGameUseCase
import com.example.clumsychicken.domain.usecase.UpdateEggsUseCase
import com.example.clumsychicken.domain.usecase.UpdateHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameUiState(
    val score: Int = 0,
    val timeLeft: Int = 0,
    val eggs: List<FallingEgg> = emptyList(),
    val isPaused: Boolean = false,
    val width: Float = 0f,
    val height: Float = 0f,
    val mode: GameMode = GameMode.EASY,
    val isGameStarted: Boolean = false
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val startGameUseCase: StartGameUseCase,
    private val processTapUseCase: ProcessTapUseCase,
    private val spawnEggUseCase: SpawnEggUseCase,
    private val updateEggsUseCase: UpdateEggsUseCase,
    private val updateHighScore: UpdateHighScoreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private var config: GameConfig? = null
    private var gameLoop: Job? = null
    private var timer: Job? = null
    private var spawnAccMs = 0

    fun start(mode: GameMode) {
        gameLoop?.cancel()
        timer?.cancel()

        config = startGameUseCase(mode)
        _state.value = GameUiState(
            score = 0,
            timeLeft = config!!.timeSeconds,
            eggs = emptyList(),
            isPaused = false,
            mode = mode,
            isGameStarted = true,
            width = _state.value.width,
            height = _state.value.height
        )
        spawnAccMs = 0
        startTimer()
        startGameLoop()
    }

    fun setCanvasSize(width: Float, height: Float) {
        if (width != _state.value.width || height != _state.value.height) {
            _state.value = _state.value.copy(width = width, height = height)
        }
    }

    fun togglePause() {
        _state.value = _state.value.copy(isPaused = !_state.value.isPaused)
    }

    fun tap(x: Float, y: Float) {
        val currentState = _state.value
        if (currentState.isPaused || !currentState.isGameStarted) return

        val tapResult = processTapUseCase(
            x = x,
            y = y,
            eggs = currentState.eggs,
            currentScore = currentState.score,
            screenWidth = currentState.width,
            screenHeight = currentState.height
        )

        _state.value = currentState.copy(
            score = tapResult.newScore,
            eggs = tapResult.remainingEggs
        )
    }

    suspend fun finishAndPersist(): Int {
        val score = _state.value.score
        updateHighScore(score)
        return score
    }

    private fun startGameLoop() {
        gameLoop = viewModelScope.launch {
            while (_state.value.isGameStarted && _state.value.timeLeft > 0) {
                val currentState = _state.value
                val cfg = config ?: break

                if (!currentState.isPaused && currentState.width > 0f && currentState.height > 0f) {
                    var eggs = currentState.eggs

                    spawnAccMs += 16
                    while (spawnAccMs >= cfg.spawnMs && eggs.size < cfg.maxItems) {
                        eggs = eggs + spawnEggUseCase(cfg)
                        spawnAccMs -= cfg.spawnMs
                    }

                    if (eggs.size > 150) {
                        eggs = eggs.drop(maxOf(0, eggs.size - 150))
                    }

                    eggs = updateEggsUseCase(eggs, currentState.height)

                    _state.value = currentState.copy(eggs = eggs)
                }
                delay(16)
            }
        }
    }

    private fun startTimer() {
        timer = viewModelScope.launch {
            while (_state.value.timeLeft > 0 && _state.value.isGameStarted) {
                delay(1000)
                val currentState = _state.value
                if (!currentState.isPaused && currentState.isGameStarted) {
                    val newTimeLeft = currentState.timeLeft - 1
                    _state.value = currentState.copy(timeLeft = newTimeLeft)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameLoop?.cancel()
        timer?.cancel()
    }
}
