package com.example.kalah_1.presentation.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kalah_1.R
import com.example.kalah_1.data.database.GameResultEntity
import com.example.kalah_1.data.repository.GameRepository
import com.example.kalah_1.domain.ai.KalahAI
import com.example.kalah_1.presentation.settings.SettingsActivity
import com.example.kalah_1.presentation.statistics.StatisticsActivity
import com.example.kalah_1.ui.theme.Kalah_1Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Date

class GameActivity : ComponentActivity() {

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameRepository = GameRepository(this)

        val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: "Player 1"
        val player1Avatar = intent.getIntExtra("PLAYER1_AVATAR", R.drawable.avatar1)
        val player2Name = intent.getStringExtra("PLAYER2_NAME") ?: "Player 2"
        val player2Avatar = intent.getIntExtra("PLAYER2_AVATAR", R.drawable.avatar2)
        val gameMode = intent.getStringExtra("GAME_MODE") ?: "TWO_PLAYERS"
        val pitsPerPlayer = intent.getIntExtra("PITS_PER_PLAYER", 6)
        val stonesPerPit = intent.getIntExtra("STONES_PER_PIT", 4)
        val aiDifficulty = intent.getIntExtra("AI_DIFFICULTY", 1)
        val isVsAI = gameMode == "VS_AI"

        setContent {
            Kalah_1Theme {
                GameScreen(
                    player1Name = player1Name,
                    player1Avatar = player1Avatar,
                    player2Name = player2Name,
                    player2Avatar = player2Avatar,
                    isVsAI = isVsAI,
                    pitsPerPlayer = pitsPerPlayer,
                    stonesPerPit = stonesPerPit,
                    aiDifficulty = aiDifficulty,
                    onGameEnd = { result ->
                        saveGameResult(result, player1Name, player1Avatar, player2Name, player2Avatar, gameMode, pitsPerPlayer, stonesPerPit)
                    }
                )
            }
        }
    }

    private fun saveGameResult(
        result: GameResult,
        player1Name: String,
        player1Avatar: Int,
        player2Name: String,
        player2Avatar: Int,
        gameMode: String,
        pitsCount: Int,
        stonesPerPit: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = GameResultEntity(
                player1Name = player1Name,
                player1AvatarRes = player1Avatar,
                player2Name = player2Name,
                player2AvatarRes = player2Avatar,
                winner = result.winner,
                player1Score = result.player1Score,
                player2Score = result.player2Score,
                gameMode = gameMode,
                date = Date(),
                pitsCount = pitsCount,
                stonesPerPit = stonesPerPit
            )
            gameRepository.saveResult(entity)
        }
    }
}

data class GameResult(
    val winner: String,
    val player1Score: Int,
    val player2Score: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    player1Name: String,
    player1Avatar: Int,
    player2Name: String,
    player2Avatar: Int,
    isVsAI: Boolean,
    pitsPerPlayer: Int,
    stonesPerPit: Int,
    aiDifficulty: Int,
    onGameEnd: (GameResult) -> Unit
) {
    val context = LocalContext.current
    var gameState by remember {
        mutableStateOf(
            KalahGameState(
                pitsPerPlayer = pitsPerPlayer,
                stonesPerPit = stonesPerPit
            )
        )
    }
    var showResultDialog by remember { mutableStateOf(false) }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }
    val ai = remember { if (isVsAI) KalahAI(aiDifficulty) else null }

    // Ход AI
    LaunchedEffect(gameState.isPlayer1Turn, gameState.isGameOver, gameState.isAIThinking) {
        if (!gameState.isGameOver && isVsAI && !gameState.isPlayer1Turn && !gameState.isAIThinking) {
            gameState = gameState.copy(isAIThinking = true)

            delay(500)

            val bestMove = ai?.getBestMove(
                board = gameState.board,
                isPlayer1Turn = false,
                player1Pits = pitsPerPlayer,
                stonesPerPit = stonesPerPit
            )

            bestMove?.let { move ->
                gameState = gameState.makeMove(move)
            }

            gameState = gameState.copy(isAIThinking = false)
        }
    }

    // Проверка окончания игры
    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver && gameResult == null) {
            val player1Score = gameState.board[pitsPerPlayer]
            val player2Score = gameState.board[gameState.board.size - 1]
            val winner = when {
                player1Score > player2Score -> player1Name
                player2Score > player1Score -> player2Name
                else -> "Ничья"
            }
            val result = GameResult(winner, player1Score, player2Score)
            gameResult = result
            showResultDialog = true
            onGameEnd(result)
        }
    }

    // Получаем тему доски из настроек
    val prefs = context.getSharedPreferences("game_settings", Context.MODE_PRIVATE)
    val boardTheme = prefs.getString("board_theme", "wood") ?: "wood"
    val boardColor = if (boardTheme == "metal") Color(0xFF607D8B) else Color(0xFF8D6E63)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(boardColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Верхняя панель с кнопками
            TopAppBar(
                title = { Text("Игра Калах", color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, StatisticsActivity::class.java))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.topmenu_statistic),
                            contentDescription = "Statistics",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.topmenu_set),
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )

            // Игрок 2 (верхний)
            PlayerSection(
                name = player2Name,
                avatar = player2Avatar,
                isBottom = false,
                kalahStones = gameState.board[gameState.board.size - 1],
                modifier = Modifier.weight(1f)
            )

            // Игровое поле
            GameBoard(
                board = gameState.board,
                pitsPerPlayer = pitsPerPlayer,
                isPlayer1Turn = gameState.isPlayer1Turn,
                isGameOver = gameState.isGameOver,
                enabled = !gameState.isGameOver && !gameState.isAIThinking,
                onPitClick = { pitIndex ->
                    if (!gameState.isGameOver && !gameState.isAIThinking) {
                        if ((gameState.isPlayer1Turn && pitIndex < pitsPerPlayer) ||
                            (!gameState.isPlayer1Turn && pitIndex > pitsPerPlayer && pitIndex < gameState.board.size - 1)) {
                            if (gameState.board[pitIndex] > 0) {
                                gameState = gameState.makeMove(pitIndex)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp)
            )

            // Игрок 1 (нижний)
            PlayerSection(
                name = player1Name,
                avatar = player1Avatar,
                isBottom = true,
                kalahStones = gameState.board[pitsPerPlayer],
                modifier = Modifier.weight(1f)
            )
        }

        // Индикатор хода
        if (!gameState.isGameOver && !gameState.isAIThinking) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xCC000000))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (gameState.isPlayer1Turn) "Ход: $player1Name" else "Ход: $player2Name",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Индикатор думающего AI
        if (gameState.isAIThinking) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xCC000000))
                    .padding(16.dp)
            ) {
                Text(
                    text = "AI думает...",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // Диалог результата
        if (showResultDialog && gameResult != null) {
            GameResultDialog(
                result = gameResult!!,
                player1Name = player1Name,
                player2Name = player2Name,
                onDismiss = {
                    showResultDialog = false
                    (context as? ComponentActivity)?.finish()
                }
            )
        }
    }
}

@Composable
fun PlayerSection(
    name: String,
    avatar: Int,
    isBottom: Boolean,
    kalahStones: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = if (isBottom) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isBottom) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Card(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFF9800))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = kalahStones.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xCC4CAF50))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = kalahStones.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun GameBoard(
    board: List<Int>,
    pitsPerPlayer: Int,
    isPlayer1Turn: Boolean,
    isGameOver: Boolean,
    enabled: Boolean,
    onPitClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val topPits = board.subList(pitsPerPlayer + 1, board.size - 1).reversed()
    val bottomPits = board.subList(0, pitsPerPlayer)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            topPits.forEachIndexed { index, stones ->
                val actualIndex = board.size - 2 - index
                Pit(
                    stones = stones,
                    enabled = enabled && !isPlayer1Turn && !isGameOver,
                    onClick = { onPitClick(actualIndex) }
                )
                if (index < topPits.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomPits.forEachIndexed { index, stones ->
                Pit(
                    stones = stones,
                    enabled = enabled && isPlayer1Turn && !isGameOver,
                    onClick = { onPitClick(index) }
                )
                if (index < bottomPits.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun Pit(
    stones: Int,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(55.dp)
            .clickable(enabled = enabled && stones > 0) { onClick() },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = when {
                !enabled -> Color(0xFF757575)
                stones == 0 -> Color(0xFF9E9E9E)
                else -> Color(0xFF2196F3)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stones.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun GameResultDialog(
    result: GameResult,
    player1Name: String,
    player2Name: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF2C2C2C),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Игра окончена!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when (result.winner) {
                        "Ничья" -> "Ничья!"
                        player1Name -> "Победил: $player1Name!"
                        player2Name -> "Победил: $player2Name!"
                        else -> "Победил: ${result.winner}!"
                    },
                    fontSize = 18.sp,
                    color = if (result.winner == "Ничья") Color(0xFF9C27B0) else Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(player1Name, color = Color.White, fontSize = 14.sp)
                        Text(
                            text = "${result.player1Score}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(player2Name, color = Color.White, fontSize = 14.sp)
                        Text(
                            text = "${result.player2Score}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Закрыть", fontSize = 16.sp)
                }
            }
        }
    }
}

data class KalahGameState(
    val board: List<Int>,
    val isPlayer1Turn: Boolean,
    val isGameOver: Boolean,
    val isAIThinking: Boolean = false
) {
    constructor(pitsPerPlayer: Int, stonesPerPit: Int) : this(
        board = List(pitsPerPlayer) { stonesPerPit } +
                listOf(0) +
                List(pitsPerPlayer) { stonesPerPit } +
                listOf(0),
        isPlayer1Turn = true,
        isGameOver = false
    )

    fun makeMove(pitIndex: Int): KalahGameState {
        val newBoard = board.toMutableList()
        var stones = newBoard[pitIndex]
        newBoard[pitIndex] = 0
        var currentIndex = pitIndex
        val isPlayer1Move = pitIndex < board.size / 2
        val playerKalahIndex = if (isPlayer1Move) board.size / 2 else board.size - 1

        while (stones > 0) {
            currentIndex = (currentIndex + 1) % board.size
            if ((!isPlayer1Move && currentIndex == board.size / 2) ||
                (isPlayer1Move && currentIndex == board.size - 1)) {
                continue
            }
            newBoard[currentIndex]++
            stones--
        }

        if (isPlayer1Move && currentIndex < board.size / 2 && newBoard[currentIndex] == 1) {
            val oppositeIndex = board.size - 2 - currentIndex
            if (oppositeIndex > board.size / 2 && oppositeIndex < board.size - 1 && newBoard[oppositeIndex] > 0) {
                newBoard[playerKalahIndex] += newBoard[oppositeIndex] + 1
                newBoard[currentIndex] = 0
                newBoard[oppositeIndex]  = 0
            }
        } else if (!isPlayer1Move && currentIndex > board.size / 2 && currentIndex < board.size - 1 && newBoard[currentIndex] == 1) {
            val oppositeIndex = (board.size / 2 - 1) - (currentIndex - board.size / 2 - 1)
            if (oppositeIndex >= 0 && oppositeIndex < board.size / 2 && newBoard[oppositeIndex] > 0) {
                newBoard[playerKalahIndex] += newBoard[oppositeIndex] + 1
                newBoard[currentIndex] = 0
                newBoard[oppositeIndex] = 0
            }
        }

        val player1PitsEmpty = (0 until board.size / 2).all { newBoard[it] == 0 }
        val player2PitsEmpty = ((board.size / 2 + 1) until board.size - 1).all { newBoard[it] == 0 }

        val isGameOver = player1PitsEmpty || player2PitsEmpty

        if (isGameOver) {
            for (i in newBoard.indices) {
                if (i != board.size / 2 && i != board.size - 1 && newBoard[i] > 0) {
                    if (i < board.size / 2) {
                        newBoard[board.size / 2] += newBoard[i]
                    } else {
                        newBoard[board.size - 1] += newBoard[i]
                    }
                    newBoard[i] = 0
                }
            }
        }

        val extraTurn = !isGameOver && currentIndex == playerKalahIndex

        return KalahGameState(
            board = newBoard,
            isPlayer1Turn = if (extraTurn) isPlayer1Move else !isPlayer1Move,
            isGameOver = isGameOver
        )
    }
}