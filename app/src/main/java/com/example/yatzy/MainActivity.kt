package com.example.yatzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.yatzy.screen.GameScreen
import com.example.yatzy.viewmodel.KniffelViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yatzy.ui.theme.YatzyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Den State für den Dark Mode erstellen (Standard: false oder Systemeinstellung)
            var isDarkMode by remember { mutableStateOf(false) }

            // 2. Dein App-Theme umschließt alles. Es reagiert auf isDarkMode!
            // (Der Name deines Themes, z.B. YatzyTheme, steht in ui/theme/Theme.kt)
            YatzyTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: KniffelViewModel = viewModel()

                    // 3. Wir übergeben den State und die Funktion zum Umschalten an den Screen
                    GameScreen(
                        viewModel = viewModel,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}