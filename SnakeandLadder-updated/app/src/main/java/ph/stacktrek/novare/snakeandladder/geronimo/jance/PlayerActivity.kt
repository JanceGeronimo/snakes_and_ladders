package ph.stacktrek.novare.snakeandladder.geronimo.jance

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ph.stacktrek.novare.snakeandladder.geronimo.jance.adapters.PlayerAdapter
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAO
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.ActivityPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialgoueEditPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialogueAddPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.Player

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    //create variable of product adapter
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var playerDAO: PlayerDAO

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadPlayers()

        binding.fabAddPlayerButton.setOnClickListener {
            showAddPlayerDialogue().show()
        }

        binding.startGame.setOnClickListener {
            val goToMain = Intent(
                applicationContext,
                MainActivity::class.java
            )

            val bundle = Bundle()
            for (players in playerAdapter.getAllPlayer()) {
                bundle.putString(players.id, players.name)
            }

            goToMain.putExtras(bundle)

            startActivity(goToMain)
            finish()
        }
    }

    fun loadPlayers() {
//        playerDAO = PlayerDAOStubImplementation()
        playerDAO = PlayerDAOSQLLiteImplementation(applicationContext)
        playerAdapter = PlayerAdapter(applicationContext, playerDAO.getPlayers()) { player ->
            showEditPlayerDialogue(player).show()
        }

        with(binding.playersList) {
            layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = playerAdapter
        }
    }


    private fun showAddPlayerDialogue(): Dialog {
        return this.let {
            val builder = AlertDialog.Builder(it)
            val dialogueAddPlayerBinding: DialogueAddPlayerBinding =
                DialogueAddPlayerBinding.inflate(it.layoutInflater)

            with(builder) {

                setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->

                })

                setPositiveButton("ADD", DialogInterface.OnClickListener { dialog, id ->

                    val player = Player("")
                    player.name = dialogueAddPlayerBinding.playerName.text.toString()

                    val playerDAO = PlayerDAOSQLLiteImplementation(applicationContext)
                    playerDAO.addPlayer(player)

                    player.id = playerDAO.getLastInsertedId().toString()

                    playerAdapter.addPlayer(player)

                })

                setView(dialogueAddPlayerBinding.root)
                create()

            }
        }
    }

    private fun showEditPlayerDialogue(player: Player): Dialog {
        return this.let {
            val builder = AlertDialog.Builder(it)
            val dialgoueEditPlayerBinding: DialgoueEditPlayerBinding =
                DialgoueEditPlayerBinding.inflate(it.layoutInflater)

            dialgoueEditPlayerBinding.playerName.setText(player.name)

            val playerDAO = PlayerDAOSQLLiteImplementation(applicationContext)

            with(builder) {

                setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->

                })

                setNeutralButton("DELETE", DialogInterface.OnClickListener { dialog, id ->
                    val position = playerAdapter.getPlayerPostionArrayList(player)
                    playerAdapter.deletePlayer(position)
                })

                setPositiveButton("SAVE", DialogInterface.OnClickListener { dialog, id ->
                    player.name = dialgoueEditPlayerBinding.playerName.text.toString()

                    playerDAO.updatePlayer(player)
                    playerAdapter.updatePlayer(player)
                })

                setView(dialgoueEditPlayerBinding.root)
                create()
            }
        }

    }


//    fun showEditPlayerDialogue(): Dialog {
//        return this.let {
//            val builder = AlertDialog.Builder(context)
//            val dialgoueEditPlayerBinding: DialgoueEditPlayerBinding =
//                DialgoueEditPlayerBinding.inflate(LayoutInflater.from(context), null, false)
//
//            with(builder) {
//
//                setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->
//
//                })
//
//                setNeutralButton("DELETE", DialogInterface.OnClickListener { dialog, id ->
//
//                })
//
//                setPositiveButton("SAVE", DialogInterface.OnClickListener { dialog, id ->
//
//                })
//
//                setView(dialgoueEditPlayerBinding.root)
//                create()
//
//            }
//        }
//    }

}