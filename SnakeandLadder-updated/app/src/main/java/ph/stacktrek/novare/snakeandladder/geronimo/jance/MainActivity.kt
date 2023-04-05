package ph.stacktrek.novare.snakeandladder.geronimo.jance

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.adapters.PlayerAdapter
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAO
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerWinnerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.ActivityMainBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.ActivityPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialogueAddPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialogueWinnerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.Player
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.PlayerWinner
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var gamePlayers: ArrayList<Player>

    private var currentPlayerIndex: Int = 0

    private var finishLine: Int = 100

    private var defaultButtonColor: ColorStateList? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
        setSnake()
        setLadder()

        gamePlayers = ArrayList()

        var playersText: String = ""
        val bundle = intent.extras
        if (bundle != null) {
            var count: Int = 0
            for (key in bundle.keySet()) {
                val value = bundle.getString(key)
                Log.d("MyPlayers", "$key: $value")

                playersText += "${value}: Tile 0\n"

                val newPlayer = Player("$value")
                newPlayer.id = "$key"
                newPlayer.game_position = "0"
                gamePlayers.add(newPlayer)
            }
        }

        val lastNewLineIndex = playersText.lastIndexOf("\n")
        playersText = playersText.substring(0, lastNewLineIndex)

        binding.board.setText("${gamePlayers[0].name} Turn")
        binding.textViewPlayers.setText(playersText)

        playerAdapter = PlayerAdapter(applicationContext, gamePlayers) { null
        }

        binding.DiceRollButton.setOnClickListener {

            var currentPlayerPosition = gamePlayers[currentPlayerIndex].game_position.toInt()
            var previousPlayerPosition = gamePlayers[currentPlayerIndex].game_position.toInt()
            var rolledDiceResult = rollDice()
            currentPlayerPosition += rolledDiceResult

            Log.d("PlayerPostion", "PPP${previousPlayerPosition}")
            Log.d("PlayerPostion", "CPP${currentPlayerPosition}")

            gamePlayers[currentPlayerIndex].game_position =
                currentPlayerPosition.toString()

            val drawableResources = when (rolledDiceResult) {
                1 -> R.drawable.dice1
                2 -> R.drawable.dice2
                3 -> R.drawable.dice3
                4 -> R.drawable.dice4
                5 -> R.drawable.dice5
                else -> R.drawable.dice6
            }
            binding.diceImage.setImageResource(drawableResources)

            val buttonId: String
//             Ensure not exceeding 100
            if (currentPlayerPosition < finishLine) {
                buttonId = "tile$currentPlayerPosition"
            } else {
                buttonId = "tile100"
            }

            var myButton: Button = findViewById<Button>(
                resources.getIdentifier(
                    buttonId,
                    "id",
                    packageName
                )
            )

            defaultButtonColor = myButton.backgroundTintList

            for (i in 1..currentPlayerPosition) {
                if (currentPlayerPosition >= 100) {
                    break
                } else {

                    if (i >= previousPlayerPosition || previousPlayerPosition == 0) {
                        myButton = findViewById<Button>(
                            resources.getIdentifier(
                                "tile${i}",
                                "id",
                                packageName
                            )
                        )
                        myButton.backgroundTintList = ColorStateList.valueOf(Color.BLUE)
                    }


                }
            }

            binding.textStatus.setText("${gamePlayers[currentPlayerIndex].name} rolled $rolledDiceResult")

            for (snake in Snake.values()) {
                if (currentPlayerPosition == snake.start) {
                    currentPlayerPosition = snake.end
                    gamePlayers[currentPlayerIndex].game_position =
                        currentPlayerPosition.toString()


                    for (i in currentPlayerPosition..snake.start) {
                        myButton = findViewById<Button>(
                            resources.getIdentifier(
                                "tile${i}",
                                "id",
                                packageName
                            )
                        )
                        myButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    }

                    binding.textStatus.setText("${gamePlayers[currentPlayerIndex].name} entered snake ${snake.name}")
                }
            }

            for (ladder in Ladder.values()) {
                if (currentPlayerPosition == ladder.end) {
                    currentPlayerPosition = ladder.start
                    gamePlayers[currentPlayerIndex].game_position =
                        currentPlayerPosition.toString()

                    for (i in ladder.end..ladder.start) {
                        myButton = findViewById<Button>(
                            resources.getIdentifier(
                                "tile${i}",
                                "id",
                                packageName
                            )
                        )
                        myButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                    }

                    binding.textStatus.setText("${gamePlayers[currentPlayerIndex].name} entered ladder ${ladder.name}")
                }
            }

            if (currentPlayerPosition >= 100) {
                val playerWinner = PlayerWinner("")
                playerWinner.name = gamePlayers[currentPlayerIndex].name

                val playerWinnerDAo = PlayerWinnerDAOSQLLiteImplementation(applicationContext)
                playerWinnerDAo.addPlayerWinner(playerWinner)

                val winnerDialog = showWinnerDialogue(gamePlayers[currentPlayerIndex].name)

                // Override onDismiss to finish the activity after the dialog is closed
                winnerDialog.setOnDismissListener(DialogInterface.OnDismissListener {
                    finish()
                })

                winnerDialog.show()
            }

            binding.DiceRollButton.isEnabled = false

            changeCurrentPlayerIndex()

            Handler().postDelayed({

                binding.board.setText("${gamePlayers[currentPlayerIndex].name} Turn")
                binding.textViewPlayers.setText(changeTextViewPlayers())

                // Reset Tile Colors
                for (i in 1..100) {
                    myButton = findViewById<Button>(
                        resources.getIdentifier(
                            "tile${i}",
                            "id",
                            packageName
                        )
                    )
                    myButton.backgroundTintList = defaultButtonColor
                }

                //Set color list for the next player turn
                if (gamePlayers[currentPlayerIndex].game_position.toInt() != 0
                    && gamePlayers[currentPlayerIndex].game_position.toInt() != 100
                ) {
                    myButton = findViewById<Button>(
                        resources.getIdentifier(
                            "tile${gamePlayers[currentPlayerIndex].game_position}",
                            "id",
                            packageName
                        )
                    )

                    myButton.backgroundTintList = ColorStateList.valueOf(Color.BLUE)
                }

                binding.textStatus.setText("")
                binding.DiceRollButton.isEnabled = true
            }, 1500)
        }
    }

    private fun setButtons() {
        for (x in 1..100) {
            val myButton: Button

            val myTile: Int = x
            val buttonId: String = "tile$myTile"

            myButton =
                findViewById<Button>(
                    resources.getIdentifier(
                        buttonId,
                        "id",
                        packageName
                    )
                )

            myButton.setText("$x")
            myButton.setPadding(0)
        }

    }

    private fun setSnake() {
        for (snake in Snake.values()) {
            var myButton: Button

            var myTile: Int = snake.start
            var buttonId: String = "tile$myTile"
            var snakeName: String = "${snake.name.first()}${snake.name.last()}"

            myButton =
                findViewById<Button>(
                    resources.getIdentifier(
                        buttonId,
                        "id",
                        packageName
                    )
                )
            myButton.setText("${snakeName}H")

            // Set the snake end
            myTile = snake.end
            buttonId = "tile$myTile"

            myButton =
                findViewById<Button>(
                    resources.getIdentifier(
                        buttonId,
                        "id",
                        packageName
                    )
                )
            myButton.setText("${snakeName}T")
        }
    }

    private fun setLadder() {
        for (ladder in Ladder.values()) {
            var myButton: Button

            var myTile: Int = ladder.start
            var buttonId: String = "tile$myTile"
            var ladderName: String = "${ladder.name.first()}${ladder.name.last()}"

            myButton =
                findViewById<Button>(
                    resources.getIdentifier(
                        buttonId,
                        "id",
                        packageName
                    )
                )
            myButton.setText("${ladderName}E")

            // Set the ladder end
            myTile = ladder.end
            buttonId = "tile$myTile"

            myButton =
                findViewById<Button>(
                    resources.getIdentifier(
                        buttonId,
                        "id",
                        packageName
                    )
                )
            myButton.setText("${ladderName}S")
        }
    }

    private fun rollDice(): Int {
        return ((1..6).random())
    }

    enum class Snake(val start: Int, val end: Int) {
        SNAKE_1(98, 66),
        SNAKE_2(92, 53),
        SNAKE_3(62, 57),
        SNAKE_4(56, 15),
        SNAKE_5(48, 12),
    }

    enum class Ladder(val start: Int, val end: Int) {
        LADDER_1(99, 80),
        LADDER_2(88, 52),
        LADDER_3(54, 33),
        LADDER_4(38, 2),
        LADDER_5(59, 43),
        LADDER_6(14, 4),
    }

    private fun changeCurrentPlayerIndex() {
        if (currentPlayerIndex >= gamePlayers.size - 1) {
            currentPlayerIndex = 0
        } else {
            currentPlayerIndex++
        }
    }

    private fun changeTextViewPlayers(): String {

        var textViewPlayers: String = ""
        for (player in gamePlayers) {
            textViewPlayers += "${player.name} : Tile ${player.game_position}\n"
        }
        return textViewPlayers
    }

    private fun showWinnerDialogue(winnerPlayer: String): Dialog {
        return this.let {
            val builder = AlertDialog.Builder(it)
            val dialogueWinnerBinding: DialogueWinnerBinding =
                DialogueWinnerBinding.inflate(it.layoutInflater)

            dialogueWinnerBinding.playerWinner.setText("$winnerPlayer WON")

            with(builder) {


                setView(dialogueWinnerBinding.root)
                create()

            }
        }


    }
}