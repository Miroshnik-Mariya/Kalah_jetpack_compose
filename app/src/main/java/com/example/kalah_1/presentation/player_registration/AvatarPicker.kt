package com.example.kalah_1.presentation.player_registration.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kalah_1.R
import com.example.kalah_1.ui.theme.Kalah_1Theme

// Список доступных аватаров
val AVATARS = listOf(
    R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
    R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
    R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
    R.drawable.avatar10
)

@Composable
fun AvatarPickerDialog(
    avatars: List<Int> = AVATARS,
    onAvatarSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            // фон
            Image(
                painter = painterResource(id = R.drawable.main_bckground),
                contentDescription = "Background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Затемнение фона
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            // Контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Выберите аватар",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(avatars.size) { index ->
                        AvatarItem(
                            avatarRes = avatars[index],
                            onClick = { onAvatarSelected(avatars[index]) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))
                ) {
                    Text("Отмена", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AvatarItem(
    avatarRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2C2C2C).copy(alpha = 0.9f))
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar option",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun AvatarDisplay(
    avatarRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 150
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2C2C2C))
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

// Превью для диалога выбора аватара
@Preview(
    name = "Avatar Picker Dialog",
    showBackground = true,
    device = "spec:width=800dp,height=400dp,dpi=240"
)
@Composable
fun PreviewAvatarPickerDialog() {
    Kalah_1Theme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            // фон для превью
            Image(
                painter = painterResource(id = R.drawable.main_bckground),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Затемнение
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            // Контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Выберите аватар",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(9) { index ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2C2C2C).copy(alpha = 0.9f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 40.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))
                ) {
                    Text("Отмена", color = Color.White)
                }
            }
        }
    }
}

@Preview(
    name = "Avatar Display",
    showBackground = true
)
@Composable
fun PreviewAvatarDisplay() {
    Kalah_1Theme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AvatarDisplay(
                avatarRes = R.drawable.avatar1,
                onClick = {},
                size = 100
            )
            AvatarDisplay(
                avatarRes = R.drawable.avatar2,
                onClick = {},
                size = 100
            )
            AvatarDisplay(
                avatarRes = R.drawable.avatar3,
                onClick = {},
                size = 100
            )
        }
    }
}