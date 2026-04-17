package com.example.kalah_1.domain.ai

import kotlin.math.max
import kotlin.math.min

class KalahAI(private val difficulty: Int) { // 1, 2, или 3

    fun getBestMove(
        board: List<Int>,
        isPlayer1Turn: Boolean,
        player1Pits: Int,
        stonesPerPit: Int
    ): Int {
        val depth = difficulty
        val (bestMove, _) = minimax(
            board.toMutableList(),
            depth,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            true,
            isPlayer1Turn,
            player1Pits,
            stonesPerPit
        )
        return bestMove ?: getFirstAvailableMove(board, player1Pits, isPlayer1Turn)
    }

    private fun minimax(
        board: MutableList<Int>,
        depth: Int,
        alpha: Int,
        beta: Int,
        isMaximizing: Boolean,
        isAIPlayerTurn: Boolean, // true если сейчас ход AI, false если ход человека
        player1Pits: Int,
        stonesPerPit: Int
    ): Pair<Int?, Int> {

        val player1KalahIndex = player1Pits
        val player2KalahIndex = board.size - 1

        // Проверка терминального состояния
        if (depth == 0 || isGameOver(board, player1Pits)) {
            val score = if (isAIPlayerTurn) {
                board[player1KalahIndex] - board[player2KalahIndex]
            } else {
                board[player2KalahIndex] - board[player1KalahIndex]
            }
            return Pair(null, score)
        }

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            var bestMove: Int? = null
            // Определяем диапазон лунок AI (игрок 1 - верхний, игрок 2 - нижний)
            val start = if (isAIPlayerTurn) 0 else player1Pits + 1
            val end = if (isAIPlayerTurn) player1Pits else board.size - 1

            var currentAlpha = alpha
            var currentBeta = beta

            for (pit in start until end) {
                if (board[pit] > 0) {
                    val newBoard = simulateMove(board, pit, player1Pits, stonesPerPit)
                    val (_, eval) = minimax(
                        newBoard, depth - 1, currentAlpha, currentBeta, false, isAIPlayerTurn, player1Pits, stonesPerPit
                    )
                    if (eval > maxEval) {
                        maxEval = eval
                        bestMove = pit
                    }
                    currentAlpha = max(currentAlpha, eval)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return Pair(bestMove, maxEval)
        } else {
            var minEval = Int.MAX_VALUE
            var bestMove: Int? = null
            // Определяем диапазон лунок противника
            val start = if (isAIPlayerTurn) player1Pits + 1 else 0
            val end = if (isAIPlayerTurn) board.size - 1 else player1Pits

            var currentAlpha = alpha
            var currentBeta = beta

            for (pit in start until end) {
                if (board[pit] > 0) {
                    val newBoard = simulateMove(board, pit, player1Pits, stonesPerPit)
                    val (_, eval) = minimax(
                        newBoard, depth - 1, currentAlpha, currentBeta, true, isAIPlayerTurn, player1Pits, stonesPerPit
                    )
                    if (eval < minEval) {
                        minEval = eval
                        bestMove = pit
                    }
                    currentBeta = min(currentBeta, eval)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return Pair(bestMove, minEval)
        }
    }

    private fun simulateMove(
        board: MutableList<Int>,
        pitIndex: Int,
        player1Pits: Int,
        stonesPerPit: Int
    ): MutableList<Int> {
        val newBoard = board.toMutableList()
        var stones = newBoard[pitIndex]
        newBoard[pitIndex] = 0
        var currentIndex = pitIndex
        val isPlayer1Move = pitIndex < player1Pits
        val playerKalahIndex = if (isPlayer1Move) player1Pits else newBoard.size - 1

        while (stones > 0) {
            currentIndex = (currentIndex + 1) % newBoard.size
            // Пропускаем калах противника
            if ((!isPlayer1Move && currentIndex == player1Pits) ||
                (isPlayer1Move && currentIndex == newBoard.size - 1)) {
                continue
            }
            newBoard[currentIndex]++
            stones--
        }

        // Захват камней
        if (isPlayer1Move && currentIndex < player1Pits && newBoard[currentIndex] == 1) {
            val oppositeIndex = newBoard.size - 2 - currentIndex
            if (oppositeIndex > player1Pits && oppositeIndex < newBoard.size - 1 && newBoard[oppositeIndex] > 0) {
                newBoard[playerKalahIndex] += newBoard[oppositeIndex] + 1
                newBoard[currentIndex] = 0
                newBoard[oppositeIndex] = 0
            }
        } else if (!isPlayer1Move && currentIndex > player1Pits && currentIndex < newBoard.size - 1 && newBoard[currentIndex] == 1) {
            val oppositeIndex = (player1Pits - 1) - (currentIndex - player1Pits - 1)
            if (oppositeIndex >= 0 && oppositeIndex < player1Pits && newBoard[oppositeIndex] > 0) {
                newBoard[playerKalahIndex] += newBoard[oppositeIndex] + 1
                newBoard[currentIndex] = 0
                newBoard[oppositeIndex] = 0
            }
        }

        // Проверка на дополнительный ход
        val extraTurn = currentIndex == playerKalahIndex

        return newBoard
    }

    private fun isGameOver(board: List<Int>, player1Pits: Int): Boolean {
        val player1Empty = (0 until player1Pits).all { board[it] == 0 }
        val player2Empty = (player1Pits + 1 until board.size - 1).all { board[it] == 0 }
        return player1Empty || player2Empty
    }

    private fun getFirstAvailableMove(board: List<Int>, player1Pits: Int, isPlayer1Turn: Boolean): Int {
        if (isPlayer1Turn) {
            for (i in 0 until player1Pits) {
                if (board[i] > 0) return i
            }
            return 0
        } else {
            for (i in player1Pits + 1 until board.size - 1) {
                if (board[i] > 0) return i
            }
            return player1Pits + 1
        }
    }
}