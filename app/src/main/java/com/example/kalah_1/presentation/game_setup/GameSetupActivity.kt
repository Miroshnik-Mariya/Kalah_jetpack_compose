package com.example.kalah_1.presentation.game_setup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalah_1.R
import com.example.kalah_1.domain.model.GameConfig
import com.example.kalah_1.domain.model.GameMode
import com.example.kalah_1.presentation.game.GameActivity
import com.example.kalah_1.ui.theme.Kalah_1Theme

class GameSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: "Player 1"
        val player1Avatar = intent.getIntExtra("PLAYER1_AVATAR", R.drawable.avatar1)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "TWO_PLAYERS"
        val player2Name = intent.getStringExtra("PLAYER2_NAME")
        val player2Avatar = intent.getIntExtra("PLAYER2_AVATAR", R.drawable.avatar2)
        val isVsAI = gameMode == "VS_AI"

        setContent {
            Kalah_1Theme {
                GameSetupScreen(
                    isVsAI = isVsAI,
                    player1Name = player1Name,
                    player1Avatar = player1Avatar,
                    player2Name = player2Name,
                    player2Avatar = player2Avatar,
                    onStartGame = { config ->
                        val intent = Intent(this, GameActivity::class.java).apply {
                            putExtra("PLAYER1_NAME", player1Name)
                            putExtra("PLAYER1_AVATAR", player1Avatar)
                            putExtra("PLAYER2_NAME", if (isVsAI) "AI Bot" else (player2Name ?: "Player 2"))
                            putExtra("PLAYER2_AVATAR", if (isVsAI) R.drawable.robot_avatar else (player2Avatar ?: R.drawable.avatar2))
                            putExtra("GAME_MODE", gameMode)
                            putExtra("PITS_PER_PLAYER", config.pitsPerPlayer)
                            putExtra("STONES_PER_PIT", config.stonesPerPit)
                            putExtra("AI_DIFFICULTY", config.aiDifficulty)
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    @Composable
    fun GameSetupScreen(
        isVsAI: Boolean,
        player1Name: String,
        player1Avatar: Int,
        player2Name: String?,
        player2Avatar: Int,
        onStartGame: (GameConfig) -> Unit
    ) {
        var pitsCount by remember { mutableStateOf(6) }
        var stonesCount by remember { mutableStateOf(4) }
        var aiDifficulty by remember { mutableStateOf(1) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "НАСТРОЙКИ ИГРЫ",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                letterSpacing = 2.sp
            )

            // Количество лунок
            SettingsCard(
                title = "Количество лунок",
                value = pitsCount,
                minValue = 6,
                maxValue = 8,
                onValueChange = { pitsCount = it },
                icon = "⚫"
            )

            // Количество камней
            SettingsCard(
                title = "Камней в лунке",
                value = stonesCount,
                minValue = 4,
                maxValue = 6,
                onValueChange = { stonesCount = it },
                icon = "💎"
            )

            // Сложность AI (только для режима с AI)
            if (isVsAI) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🤖 Сложность бота",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DifficultyChip(
                                label = "Легко",
                                difficulty = 1,
                                selected = aiDifficulty == 1,
                                onClick = { aiDifficulty = 1 },
                                modifier = Modifier.weight(1f)
                            )
                            DifficultyChip(
                                label = "Средне",
                                difficulty = 2,
                                selected = aiDifficulty == 2,
                                onClick = { aiDifficulty = 2 },
                                modifier = Modifier.weight(1f)
                            )
                            DifficultyChip(
                                label = "Сложно",
                                difficulty = 3,
                                selected = aiDifficulty == 3,
                                onClick = { aiDifficulty = 3 },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val mode = if (isVsAI) GameMode.VsAI else GameMode.TwoPlayers
                    onStartGame(
                        GameConfig(
                            mode = mode,
                            pitsPerPlayer = pitsCount,
                            stonesPerPit = stonesCount,
                            aiDifficulty = if (isVsAI) aiDifficulty else 1
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("НАЧАТЬ ИГРУ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun SettingsCard(
        title: String,
        value: Int,
        minValue: Int,
        maxValue: Int,
        onValueChange: (Int) -> Unit,
        icon: String
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$icon $title",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (value > minValue) onValueChange(value - 1) },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF424242))
                    ) {
                        Text("-", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = value.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.width(60.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    IconButton(
                        onClick = { if (value < maxValue) onValueChange(value + 1) },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF424242))
                    ) {
                        Text("+", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    @Composable
    fun DifficultyChip(
        label: String,
        difficulty: Int,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = {
                Text(
                    label,
                    color = if (selected) Color.White else Color.Gray,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = modifier,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4CAF50),
                containerColor = Color(0xFF424242)
            )
        )
    }
}