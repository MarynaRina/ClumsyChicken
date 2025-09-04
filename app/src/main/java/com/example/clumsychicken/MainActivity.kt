package com.example.clumsychicken

import com.example.clumsychicken.core.ui.theme.ClumsyChickenTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.clumsychicken.core.navigation.GameMode
import com.example.clumsychicken.core.navigation.Screen
import com.example.clumsychicken.ui.loading.LoadingScreen
import com.example.clumsychicken.ui.start.StartScreen
import com.example.clumsychicken.ui.game.GameScreen
import com.example.clumsychicken.ui.result.ResultScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ClumsyChickenTheme { App() } }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(
                onPlayEasy = {
                    nav.currentBackStackEntry?.savedStateHandle?.set("mode", GameMode.EASY)
                    nav.navigate(Screen.Loading.route)
                },
                onPlayHard = {
                    nav.currentBackStackEntry?.savedStateHandle?.set("mode", GameMode.HARD)
                    nav.navigate(Screen.Loading.route)
                }
            )
        }
        composable(Screen.Loading.route) {
            val mode = nav.previousBackStackEntry?.savedStateHandle?.get<GameMode>("mode") ?: GameMode.EASY
            LoadingScreen(navController = nav, mode = mode)
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(navArgument(Screen.ARG_MODE) { type = NavType.StringType })
        ) { backStackEntry ->
            val m = GameMode.valueOf(backStackEntry.arguments?.getString(Screen.ARG_MODE) ?: GameMode.EASY.name)
            GameScreen(
                mode = m,
                onFinish = { score ->
                    nav.navigate("result/$score/${m.name}") {
                        popUpTo(Screen.Game.route.substringBefore("/{")) { inclusive = true }
                    }
                },
                onExit = { nav.navigate(Screen.Start.route) }
            )
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument(Screen.ARG_SCORE) { type = NavType.IntType },
                navArgument(Screen.ARG_MODE) { type = NavType.StringType }
            )
        ) { entry ->
            val score = entry.arguments?.getInt(Screen.ARG_SCORE) ?: 0
            val mode = GameMode.valueOf(entry.arguments?.getString(Screen.ARG_MODE) ?: GameMode.EASY.name)
            ResultScreen(
                score = score,
                onPlayAgain = { nav.navigate("game/${mode.name}") },
                onMainMenu = { nav.navigate(Screen.Start.route) }
            )
        }
    }
}