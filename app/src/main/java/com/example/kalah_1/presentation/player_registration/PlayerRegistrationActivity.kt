package com.example.kalah_1.presentation.player_registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kalah_1.R
import com.example.kalah_1.domain.model.Player
import com.example.kalah_1.presentation.game_setup.GameSetupActivity
import com.example.kalah_1.ui.theme.Kalah_1Theme
import java.util.UUID

class PlayerRegistrationActivity : ComponentActivity() {

    companion object {
        private val AVATARS = listOf(
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
            R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
            R.drawable.avatar10
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "TWO_PLAYERS"
        val isVsAI = gameMode == "VS_AI"

        setContent {
            Kalah_1Theme {
                PlayerRegistrationScreen(
                    isVsAI = isVsAI,
                    onPlayersRegistered = { players ->
                        val intent = Intent(this, GameSetupActivity::class.java).apply {
                            putExtra("PLAYER1_NAME", players[0].name)
                            putExtra("PLAYER1_AVATAR", players[0].avatarResId)
                            putExtra("GAME_MODE", gameMode)
                            if (!isVsAI && players.size > 1) {
                                putExtra("PLAYER2_NAME", players[1].name)
                                putExtra("PLAYER2_AVATAR", players[1].avatarResId)
                            }
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    @Composable
    fun PlayerRegistrationScreen(
        isVsAI: Boolean,
        onPlayersRegistered: (List<Player>) -> Unit
    ) {
        var player1Name by remember { mutableStateOf("") }
        var player1Avatar by remember { mutableStateOf(AVATARS[0]) }
        var showAvatarPicker by remember { mutableStateOf(false) }
        var currentPlayer by remember { mutableStateOf(1) }

        var player2Name by remember { mutableStateOf("") }
        var player2Avatar by remember { mutableStateOf(AVATARS[1]) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (currentPlayer == 1) "ИГРОК 1" else "ИГРОК 2",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2C2C2C))
                    .clickable { showAvatarPicker = true }
                    .padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = if (currentPlayer == 1) player1Avatar else player2Avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "Нажмите, чтобы выбрать аватар",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = if (currentPlayer == 1) player1Name else player2Name,
                onValueChange = { if (currentPlayer == 1) player1Name = it else player2Name = it },
                label = { Text("Имя игрока", color = Color.Gray) },
                placeholder = { Text("Введите имя", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = OutlinedTextFieldDefaults.colors(
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

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    when {
                        currentPlayer == 1 && player1Name.isNotBlank() && !isVsAI -> {
                            currentPlayer = 2
                        }
                        currentPlayer == 1 && player1Name.isNotBlank() && isVsAI -> {
                            val players = listOf(
                                Player(UUID.randomUUID().toString(), player1Name, player1Avatar),
                                Player("ai", "AI Bot", R.drawable.robot_avatar, isAI = true)
                            )
                            onPlayersRegistered(players)
                        }
                        currentPlayer == 2 && player2Name.isNotBlank() -> {
                            val players = listOf(
                                Player(UUID.randomUUID().toString(), player1Name, player1Avatar),
                                Player(UUID.randomUUID().toString(), player2Name, player2Avatar)
                            )
                            onPlayersRegistered(players)
                        }
                    }
                },
                enabled = if (currentPlayer == 1) player1Name.isNotBlank() else player2Name.isNotBlank(),
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
                    text = if (currentPlayer == 1 && !isVsAI) "Далее" else "Начать игру",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showAvatarPicker) {
            AvatarPickerDialog(
                avatars = AVATARS,
                onAvatarSelected = { avatar ->
                    if (currentPlayer == 1) player1Avatar = avatar else player2Avatar = avatar
                    showAvatarPicker = false
                },
                onDismiss = { showAvatarPicker = false }
            )
        }
    }

    @Composable
    fun AvatarPickerDialog(
        avatars: List<Int>,
        onAvatarSelected: (Int) -> Unit,
        onDismiss: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF2C2C2C),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(avatars.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF424242))
                                    .clickable { onAvatarSelected(avatars[index]) }
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = avatars[index]),
                                    contentDescription = "Avatar option",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Отмена")
                    }
                }
            }
        }
    }
}