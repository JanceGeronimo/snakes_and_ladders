package ph.stacktrek.novare.snakeandladder.geronimo.jance.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ph.stacktrek.novare.snakeandladder.geronimo.jance.GameFragment
import ph.stacktrek.novare.snakeandladder.geronimo.jance.PlayerActivity
import ph.stacktrek.novare.snakeandladder.geronimo.jance.dao.PlayerDAOSQLLiteImplementation
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.ActivityPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialgoueEditPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialogueAddPlayerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.DialogueWinnerBinding
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.Player
import ph.stacktrek.novare.snakeandladder.geronimo.jance.databinding.PlayerListBinding

class PlayerAdapter(
    private val context: Context,
    private var playerList: ArrayList<Player>,
    private val onPlayerClick: (player: Player) -> Unit,
) :
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    fun deletePlayer(position: Int) {
        playerList.removeAt(position)

        //update the UI for the changes
        notifyItemRemoved(position)
    }

    fun addPlayer(player: Player) {
        playerList.add(playerList.size, player)
        notifyItemInserted(playerList.size)
    }

    //additional method
    fun getAllPlayer(): ArrayList<Player> {
        return playerList
    }

    fun getPlayerPostionArrayList(player: Player): Int {
        var index = -1
        for (i in playerList.indices) {
            if (playerList[i].id == player.id.toString()) {
                index = i
                break
            }
        }
        return index
    }

    fun updatePlayer(player: Player) {
        val position = getPlayerPostionArrayList(player)
        playerList[position].name = player.name
        notifyItemChanged(position)
//        notifyDataSetChanged()
//        forceUpdateUI()
    }

    fun forceUpdateUI() {
        for (players in 1..playerList.size) {
            notifyItemChanged(players)
        }
    }


    //below are fixed methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerAdapter.ViewHolder {
        val playerItemBinding = PlayerListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(playerItemBinding, parent)
    }

    override fun onBindViewHolder(holder: PlayerAdapter.ViewHolder, position: Int) {
        holder.bindItems(playerList[position])
    }

    override fun getItemCount(): Int = playerList.size

    inner class ViewHolder(
        private val playerItemBinding: PlayerListBinding,
        private val parent: ViewGroup,
    ) :
        RecyclerView.ViewHolder(playerItemBinding.root) {

        fun bindItems(player: Player) {
            playerItemBinding.playerName.text = player.name

            // View details button
            playerItemBinding.playersList.setOnClickListener {
                onPlayerClick(player)
            }
        }
    }
}

/*
fun showEditPlayerDialogue(player: Player): Dialog {
return this.let {
val builder = AlertDialog.Builder(parent.context)
val dialgoueEditPlayerBinding: DialgoueEditPlayerBinding =
DialgoueEditPlayerBinding.inflate(
LayoutInflater.from(parent.context),
parent,
false
)

dialgoueEditPlayerBinding.playerName.setText(player.name)

val playerDAO = PlayerDAOSQLLiteImplementation(parent.context)
val playerAdapter =
PlayerAdapter(parent.context, playerDAO.getPlayers())

with(builder) {

setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->

})

setNeutralButton("DELETE", DialogInterface.OnClickListener { dialog, id ->

})

setPositiveButton("SAVE", DialogInterface.OnClickListener { dialog, id ->
player.name = dialgoueEditPlayerBinding.playerName.text.toString()

//                        playerDAO.updatePlayer(player)
playerAdapter.updatePlayer(player)
})

setView(dialgoueEditPlayerBinding.root)
create()
}
}

}
*/


