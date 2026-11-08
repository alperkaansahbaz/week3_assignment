package com.example.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week3_assignment.ui.screens.SmartHomeScreen
import com.example.week3_assignment.ui.theme.Week3AssignmentTheme
import com.example.week3_assignment.ui.viewmodel.SmartHomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week3AssignmentTheme {
                val smartHomeViewModel: SmartHomeViewModel = viewModel()
                val uiState by smartHomeViewModel.uiState.collectAsStateWithLifecycle()

                SmartHomeScreen(
                    uiState = uiState,
                    onSearchQueryChange = smartHomeViewModel::onSearchQueryChanged,
                    onRoomSelected = smartHomeViewModel::onRoomSelected,
                    onToggleDevice = smartHomeViewModel::toggleDeviceStatus
                )
            }
        }
    }
}
