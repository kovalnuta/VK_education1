package com.example.vk_education1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vk_education1.presentation.ui.AppDetailsScreenWithId
import com.example.vk_education1.presentation.ui.ListScreenWithViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ListScreenWithViewModel(
                            onAppClick = { id ->
                                navController.navigate("details/$id")
                            },
                            onShowcaseAppClick = { app ->
                                navController.navigate("details/${app.id}")
                            }
                        )
                    }
                    composable(
                        route = "details/{appId}",
                        arguments = listOf(navArgument("appId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val appId = backStackEntry.arguments?.getString("appId") ?: ""
                        AppDetailsScreenWithId(
                            appId = appId,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}