package com.example.kalah_1.presentation.main_menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kalah_1.R
import com.example.kalah_1.domain.model.GameMode
import com.example.kalah_1.presentation.main_menu.component.UpSmallMenu
import com.example.kalah_1.presentation.player_registration.PlayerRegistrationActivity
import com.example.kalah_1.presentation.settings.SettingsActivity
import com.example.kalah_1.presentation.statistics.StatisticsActivity
import com.example.kalah_1.ui.theme.Kalah_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kalah_1Theme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // фон
            Image(
                painter = painterResource(id = R.drawable.main_bckground),
                contentDescription = "Background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            //верхнее меню
            UpSmallMenu(
                modifier = Modifier
                    .align(Alignment.TopStart)
            )

            // Контейнер для кнопок
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Center)
                    .fillMaxHeight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Кнопка "Играть вдвоем"
                IconButton(
                    onClick = {
                        startPlayerRegistration(context, GameMode.TwoPlayers)
                    },
                    modifier = Modifier
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_play_together),
                        contentDescription = "Play Together",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Кнопка "Играть с ИИ"
                IconButton(
                    onClick = {
                        startPlayerRegistration(context, GameMode.VsAI)
                    },
                    modifier = Modifier
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_play_ai),
                        contentDescription = "Play vs AI",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Кнопка "Настройки"
                IconButton(
                    onClick = {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    },
                    modifier = Modifier
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_setting_main_menu),
                        contentDescription = "Settings",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }


            }
        }
    }

    private fun startPlayerRegistration(context: android.content.Context, mode: GameMode) {
        val intent = Intent(context, PlayerRegistrationActivity::class.java).apply {
            putExtra("GAME_MODE", when (mode) {
                is GameMode.TwoPlayers -> "TWO_PLAYERS"
                is GameMode.VsAI -> "VS_AI"
            })
        }
        context.startActivity(intent)
    }

    @Preview(
        name = "Horizontal Preview",
        showBackground = true,
        device = "id:pixel_6_pro",
        widthDp = 800,
        heightDp = 400
    )
    @Composable
    fun GreetingPreview() {
        Kalah_1Theme {
            MainScreen()
        }
    }
}