package com.example.kalah_1.presentation.player_registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalah_1.R
import com.example.kalah_1.domain.model.Player
import com.example.kalah_1.presentation.game_setup.GameSetupActivity
import com.example.kalah_1.presentation.player_registration.component.AvatarPickerDialog
import com.example.kalah_1.presentation.player_registration.component.AVATARS
import com.example.kalah_1.ui.theme.Kalah_1Theme
import java.util.UUID

class PlayerRegistrationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "TWO_PLAYERS"
        val isVsAI = gameMode == "VS_AI"
        val playerNumber = intent.getIntExtra("PLAYER_NUMBER", 1)

        setContent {
            Kalah_1Theme {
                PlayerRegistrationScreen(
                    isVsAI = isVsAI,
                    playerNumber = playerNumber,
                    onPlayerRegistered = { player ->
                        if (isVsAI) {
                            val intent = Intent(this, GameSetupActivity::class.java).apply {
                                putExtra("PLAYER1_NAME", player.name)
                                putExtra("PLAYER1_AVATAR", player.avatarResId)
                                putExtra("GAME_MODE", gameMode)
                                putExtra("PLAYER2_NAME", "AI Bot")
                                putExtra("PLAYER2_AVATAR", R.drawable.robot_avatar)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            if (playerNumber == 1) {
                                val intent = Intent(this, PlayerRegistrationActivity::class.java).apply {
                                    putExtra("GAME_MODE", gameMode)
                                    putExtra("PLAYER_NUMBER", 2)
                                    putExtra("PLAYER1_NAME", player.name)
                                    putExtra("PLAYER1_AVATAR", player.avatarResId)
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: "Player 1"
                                val player1Avatar = intent.getIntExtra("PLAYER1_AVATAR", AVATARS[0])

                                val intent = Intent(this, GameSetupActivity::class.java).apply {
                                    putExtra("PLAYER1_NAME", player1Name)
                                    putExtra("PLAYER1_AVATAR", player1Avatar)
                                    putExtra("PLAYER2_NAME", player.name)
                                    putExtra("PLAYER2_AVATAR", player.avatarResId)
                                    putExtra("GAME_MODE", gameMode)
                                }
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PlayerRegistrationScreen(
    isVsAI: Boolean,
    playerNumber: Int = 1,
    onPlayerRegistered: (Player) -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    var playerAvatar by remember { mutableStateOf(AVATARS[0]) }
    var showAvatarPicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.main_bckground),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Центрируем доску
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Доска-фон
            Image(
                painter = painterResource(id = R.drawable.bck_desk),
                contentDescription = "Background desk",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f),
                contentScale = ContentScale.Fit
            )

            // Контент поверх доски - РЯДАМИ (горизонтально)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Заголовок
                Text(
                    text = "РЕГИСТРАЦИЯ",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    letterSpacing = 2.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 0f
                        )
                    )
                )

                // Первый ряд: текст "Введите имя" + поле ввода
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Введите имя:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 0f
                            )
                        ),
                        modifier = Modifier.width(130.dp)
                    )

                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = { Text("Имя игрока", color = Color.Gray, fontSize = 12.sp) },
                        placeholder = { Text("Введите имя", color = Color.Gray, fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                        colors = colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color(0xFF4CAF50),
                            unfocusedLabelColor = Color.Gray,
                            focusedContainerColor = Color(0xFF2C2C2C),
                            unfocusedContainerColor = Color(0xFF2C2C2C),
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Второй ряд: текст "Выберите аватар" + аватар
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Выберите аватар:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 0f
                            )
                        ),
                        modifier = Modifier.width(130.dp)
                    )

                    // Аватар
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF2C2C2C))
                                .clickable { showAvatarPicker = true }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = playerAvatar),
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = "Нажмите, чтобы выбрать",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка далее
                Button(
                    onClick = {
                        if (playerName.isNotBlank()) {
                            onPlayerRegistered(
                                Player(UUID.randomUUID().toString(), playerName, playerAvatar)
                            )
                        }
                    },
                    enabled = playerName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Далее",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Диалог выбора аватара
    if (showAvatarPicker) {
        AvatarPickerDialog(
            avatars = AVATARS,
            onAvatarSelected = { avatar ->
                playerAvatar = avatar
                showAvatarPicker = false
            },
            onDismiss = { showAvatarPicker = false }
        )
    }
}

// Превью для экрана регистрации - РЯДАМИ
@Preview(
    name = "Player Registration Preview",
    showBackground = true,
    device = "spec:width=800dp,height=400dp,dpi=240"
)
@Composable
fun PreviewPlayerRegistration() {
    Kalah_1Theme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Фоновое изображение
            Image(
                painter = painterResource(id = R.drawable.main_bckground),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Центрируем доску
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Доска-фон
                Image(
                    painter = painterResource(id = R.drawable.bck_desk),
                    contentDescription = "Background desk",
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f),
                    contentScale = ContentScale.Fit
                )

                // Контент поверх доски - РЯДАМИ
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Заголовок
                    Text(
                        text = "РЕГИСТРАЦИЯ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 0f
                            )
                        )
                    )

                    // Первый ряд: текст "Введите имя" + поле ввода
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Введите имя:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 0f
                                )
                            ),
                            modifier = Modifier.width(130.dp)
                        )

                        var text by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Имя игрока", color = Color.Gray, fontSize = 12.sp) },
                            placeholder = { Text("Введите имя", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                            colors = colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = Color(0xFF4CAF50),
                                unfocusedLabelColor = Color.Gray,
                                focusedContainerColor = colorResource(R.color.enter_name_bck),
                                unfocusedContainerColor = colorResource(R.color.enter_name_bck),
                                focusedBorderColor = Color(0xFF4CAF50),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Второй ряд: текст "Выберите аватар" + аватар
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Выберите аватар:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 0f
                                )
                            ),
                            modifier = Modifier.width(130.dp)
                        )

                        // Аватар
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF2C2C2C))
                                    .clickable { }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👤", fontSize = 50.sp)
                            }

                            Text(
                                text = "Нажмите, чтобы выбрать",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Кнопка далее
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Далее", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}