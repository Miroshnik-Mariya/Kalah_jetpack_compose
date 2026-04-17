package com.example.kalah_1.presentation.statistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.kalah_1.R
import com.example.kalah_1.data.database.GameResultEntity
import com.example.kalah_1.data.repository.GameRepository
import com.example.kalah_1.ui.theme.Kalah_1Theme
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : ComponentActivity() {

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameRepository = GameRepository(this)

        setContent {
            Kalah_1Theme {
                StatisticsScreen(
                    onClearHistory = {
                        runBlocking {
                            gameRepository.clearHistory()
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StatisticsScreen(
        onClearHistory: () -> Unit
    ) {
        var results by remember { mutableStateOf<List<GameResultEntity>>(emptyList()) }
        var showClearDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            gameRepository.getAllResults().collect { newResults ->
                results = newResults
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
        ) {
            TopAppBar(
                title = { Text("Статистика", color = Color.White) },
                actions = {
                    if (results.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = "Clear history",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C2C2C)
                )
            )

            if (results.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Нет сохраненных игр",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Сыграйте партию, чтобы увидеть статистику",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(results) { result ->
                        GameResultCard(result)
                    }
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Очистить историю") },
                text = { Text("Вы уверены, что хотите удалить всю историю игр?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onClearHistory()
                            showClearDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showClearDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }

    @Composable
    fun GameResultCard(result: GameResultEntity) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val isVsAI = result.gameMode == "VS_AI"

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dateFormat.format(result.date),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = if (isVsAI) "🤖 Против ИИ" else "👥 Два игрока",
                        fontSize = 12.sp,
                        color = Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = result.player1AvatarRes),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = result.player1Name,
                            fontSize = 12.sp,
                            color = Color.White,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${result.player1Score}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (result.winner == result.player1Name) Color(0xFF4CAF50) else Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF424242))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "VS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = result.player2AvatarRes),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = result.player2Name,
                            fontSize = 12.sp,
                            color = Color.White,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "${result.player2Score}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (result.winner == result.player2Name) Color(0xFFFF9800) else Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val winnerColor = when {
                    result.winner == "Ничья" -> Color(0xFF9C27B0)
                    result.winner == result.player1Name -> Color(0xFF4CAF50)
                    else -> Color(0xFFFF9800)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(winnerColor)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (result.winner == "Ничья") "🤝 Ничья!" else "🏆 Победил: ${result.winner}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "⚫ Лунок: ${result.pitsCount} | 💎 Камней: ${result.stonesPerPit}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}