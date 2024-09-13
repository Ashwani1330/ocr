package com.example.ocr.utils

import MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ocr.view.ScannedCardsScreen
import com.example.ocr.viewModel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = "cameraPreview") {
        composable("cameraPreview") {
            MainScreen(viewModel = viewModel, navController = navController)
        }
        composable("scannedCards") {
            ScannedCardsScreen(viewModel = viewModel)
        }
    }
}