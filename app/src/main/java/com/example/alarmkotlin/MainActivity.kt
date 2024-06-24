package com.example.alarmkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alarmkotlin.ui.theme.AlarmKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.homeScreen, builder = {
                composable(Routes.homeScreen) {
                    HomeScreen(
                        onAddAlarmClick = {
                            navController.navigate(Routes.createAlarm)
                        }
//                        navController
                    )
                }
                composable(Routes.createAlarm) {
                    CreateAlarmScreen()
                }
            })
        }
    }
}
