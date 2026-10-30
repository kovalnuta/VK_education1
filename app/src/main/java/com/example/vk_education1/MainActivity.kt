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
import com.example.vk_education1.data.repository.AppRepositoryImpl
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.presentation.ui.AppDetailsScreen
import com.example.vk_education1.presentation.ui.ListScreenWithViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val repository = AppRepositoryImpl()
                val allApps = repository.getApps()

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        ListScreenWithViewModel(
                            onAppClick = { index -> navController.navigate("details/$index") },
                            onShowcaseAppClick = { app: AppInfo ->
                                val index = allApps.indexOf(app)
                                if (index != -1) navController.navigate("details/$index")
                            }
                        )
                    }
                    composable(
                        route = "details/{appIndex}",
                        arguments = listOf(navArgument("appIndex") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val index = backStackEntry.arguments?.getInt("appIndex") ?: 0
                        val app = allApps.getOrNull(index)
                        if (app != null) {
                            AppDetailsScreen(app = app, onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}