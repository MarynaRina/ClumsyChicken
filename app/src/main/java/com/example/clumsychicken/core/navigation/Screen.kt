package com.example.clumsychicken.core.navigation

sealed class Screen(val route: String) {
    data object Start : Screen("start")
    data object Loading : Screen("loading")
    data object Game : Screen("game/{mode}")
    data object Result : Screen("result/{score}/{mode}")
    companion object {
        const val ARG_MODE = "mode"
        const val ARG_SCORE = "score"
    }
}

enum class GameMode { EASY, HARD }