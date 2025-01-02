package com.example.tictactoegame

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button
    private lateinit var currentPlayer: String
    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameBoard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize currentPlayer here after onCreate
        currentPlayer = getString(R.string.player_x)

        initializeViews()
        setupButtons()
    }

    private fun initializeViews() {
        statusTextView = findViewById(R.id.game_activity_status_textView)
        playAgainButton = findViewById(R.id.game_activity_play_again_button)

        // Initialize the buttons array
        buttons = Array(3) { row ->
            Array(3) { col ->
                val buttonId = when {
                    row == 0 && col == 0 -> R.id.game_activity_top_left_button
                    row == 0 && col == 1 -> R.id.game_activity_top_center_button
                    row == 0 && col == 2 -> R.id.game_activity_top_right_button
                    row == 1 && col == 0 -> R.id.game_activity_middle_left_button
                    row == 1 && col == 1 -> R.id.game_activity_middle_center_button
                    row == 1 && col == 2 -> R.id.game_activity_middle_right_button
                    row == 2 && col == 0 -> R.id.game_activity_bottom_left_button
                    row == 2 && col == 1 -> R.id.game_activity_bottom_center_button
                    else -> R.id.game_activity_bottom_right_button
                }
                findViewById(buttonId)
            }
        }
    }

    private fun setupButtons() {
        // Set up click listeners for game buttons
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setOnClickListener {
                    onCellClicked(i, j)
                }
            }
        }

        // Set up play again button
        playAgainButton.setOnClickListener {
            resetGame()
        }
    }

    private fun onCellClicked(row: Int, col: Int) {
        if (gameActive && buttons[row][col].text.isEmpty()) {
            buttons[row][col].text = currentPlayer

            if (checkWin(row, col)) {
                statusTextView.text = getString(R.string.player_wins, currentPlayer)
                gameActive = false
            } else if (checkDraw()) {
                statusTextView.text = getString(R.string.game_draw)
                gameActive = false
            } else {
                currentPlayer = if (currentPlayer == getString(R.string.player_x))
                    getString(R.string.player_o) else getString(R.string.player_x)
                statusTextView.text = if (currentPlayer == getString(R.string.player_x))
                    getString(R.string.player_x_turn) else getString(R.string.player_o_turn)
            }
        }
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        // Check row
        if (buttons[row][0].text == currentPlayer &&
            buttons[row][1].text == currentPlayer &&
            buttons[row][2].text == currentPlayer) {
            return true
        }

        // Check column
        if (buttons[0][col].text == currentPlayer &&
            buttons[1][col].text == currentPlayer &&
            buttons[2][col].text == currentPlayer) {
            return true
        }

        // Check diagonals
        if (row == col &&
            buttons[0][0].text == currentPlayer &&
            buttons[1][1].text == currentPlayer &&
            buttons[2][2].text == currentPlayer) {
            return true
        }

        if (row + col == 2 &&
            buttons[0][2].text == currentPlayer &&
            buttons[1][1].text == currentPlayer &&
            buttons[2][0].text == currentPlayer) {
            return true
        }

        return false
    }

    private fun checkDraw(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (buttons[i][j].text.isEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    private fun resetGame() {
        // Clear all buttons
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].text = ""
            }
        }

        // Reset game state
        currentPlayer = getString(R.string.player_x)
        gameActive = true
        statusTextView.text = getString(R.string.player_x_turn)
    }
}