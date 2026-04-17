package com.example.kalah_1.presentation.main_menu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kalah_1.R
import com.example.kalah_1.ui.theme.Kalah_1Theme

/*
class up_small_menu {

}*/

@Composable
fun UpSmallMenu(
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit = { println("Info clicked") },
    onSoundClick: () -> Unit = { println("Sound clicked") },
    onSettingsClick: () -> Unit = { println("Settings clicked") }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, start = 20.dp),
            //.height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp), // отступ между кнопками// отступ между кнопками
    ) {
        IconButton(
            onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
//            modifier = Modifier.Companion
//                .width(253.dp)
//                .height(80.dp)
            modifier = Modifier.size(70.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.topmenu_info),
                contentDescription = "Button №1",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )

        }

        IconButton(
            onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
//            modifier = Modifier.Companion
//                .width(253.dp)
//                .height(80.dp)
            modifier = Modifier.size(70.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.topmenu_sound_on),
                contentDescription = "Button №1",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

        }

        IconButton(
            onClick = { println("Поздравляем! Ты нажал на кнопку №1!") },
//            modifier = Modifier.Companion
//                .width(253.dp)
//                .height(80.dp)
            modifier = Modifier.size(70.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.topmenu_statistic),
                contentDescription = "Button №1",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Companion.Fit
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kalah_1Theme {
        UpSmallMenu()
    }
}