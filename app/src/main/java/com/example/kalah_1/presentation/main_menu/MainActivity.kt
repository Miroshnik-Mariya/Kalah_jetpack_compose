package com.example.kalah_1.presentation.main_menu

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kalah_1.R
import com.example.kalah_1.presentation.main_menu.component.UpSmallMenu
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
        Box(
            modifier = Modifier.Companion.fillMaxSize(),
            //contentAlignment = Alignment.Center // центрируем содержимое
        ) {
            // фон
            Image(
                painter = painterResource(id = R.drawable.main_bckground),
                contentDescription = "Background image",
                modifier = Modifier.Companion.fillMaxSize(),
                contentScale = ContentScale.Companion.Crop
            )

            //верхнее меню
            UpSmallMenu(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    //.padding(top = 16.dp, start = 20.dp)
            )

            // Контейнер для кнопок размером 253x245 (по макету из Figma)
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Center)
//                    .width(253.dp)   // ширина из Figma
//                    .height(245.dp)  // высота из Figma
//                    .padding(horizontal = 8.dp, vertical = 12.dp),
                    //.fillMaxWidth(0.7f)
                    .fillMaxHeight(0.7f),
                //.padding(vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly // равномерное распределение кнопок
            ) {
                IconButton(
                    onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
                    modifier = Modifier.Companion
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_play_together),
                        contentDescription = "Button №1",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Companion.Fit
                    )

                }

                IconButton(
                    onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
                    modifier = Modifier.Companion
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_play_ai),
                        contentDescription = "Button №1",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Companion.Fit
                    )

                }

                IconButton(
                    onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
                    modifier = Modifier.Companion
                        .width(253.dp)
                        .height(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_setting_main_menu),
                        contentDescription = "Button №1",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Companion.Fit
                    )

                }
            }
        }

    }

    @Preview(
        name = "Horizontal Preview",
        showBackground = true,
        device = "id:pixel_6_pro",
        widthDp = 800,  // ширина для горизонтального режима
        heightDp = 400   // высота для горизонтального режима
    )
    @Composable
    fun GreetingPreview() {
        Kalah_1Theme {
            MainScreen()
        }
    }
}