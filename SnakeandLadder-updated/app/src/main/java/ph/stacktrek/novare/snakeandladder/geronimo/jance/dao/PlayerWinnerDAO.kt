package ph.stacktrek.novare.snakeandladder.geronimo.jance.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.PlayerWinner

interface PlayerWinnerDAO {
    fun addPlayerWinner(playerWinner: PlayerWinner)
    fun getPlayersWinner(): ArrayList<PlayerWinner>
    fun updatePlayer(playerWinner: PlayerWinner)
    fun deletePlayer(playerWinner: PlayerWinner)
    fun get5LatestPlayersWinner(): ArrayList<PlayerWinner>
}

class PlayerWinnerDAOSQLLiteImplementation(var context: Context) : PlayerWinnerDAO {
    override fun addPlayerWinner(playerWinner: PlayerWinner) {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TABLE_PLAYER_WINNER_NAME, playerWinner.name)

        var status = db.insert(
            DatabaseHandler.TABLE_PLAYER_WINNER,
            null,
            contentValues
        )
        db.close()
    }

    override fun getPlayersWinner(): ArrayList<PlayerWinner> {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var result = ArrayList<PlayerWinner>()
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.TABLE_PLAYER_WINNER_ID,
            DatabaseHandler.TABLE_PLAYER_WINNER_NAME
        )

        try {
            cursor = db.query(
                DatabaseHandler.TABLE_PLAYER_WINNER,
                columns,
                null,
                null,
                null,
                null,
                null
            )

        } catch (sqlException: SQLException) {
            db.close()
            return result
        }

        var player: PlayerWinner
        if (cursor.moveToFirst()) {
            do {
                player = PlayerWinner("")
                player.id = cursor.getInt(0).toString()
                player.name = cursor.getString(1)
                result.add(player)
            } while (cursor.moveToNext())
        }
        return result
    }

    override fun updatePlayer(playerWinner: PlayerWinner) {
        TODO("Not yet implemented")
    }

    override fun deletePlayer(playerWinner: PlayerWinner) {
        TODO("Not yet implemented")
    }

    override fun get5LatestPlayersWinner(): ArrayList<PlayerWinner> {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var result = ArrayList<PlayerWinner>()
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.TABLE_PLAYER_WINNER_ID,
            DatabaseHandler.TABLE_PLAYER_WINNER_NAME
        )

        try {
            cursor = db.query(
                DatabaseHandler.TABLE_PLAYER_WINNER,
                columns,
                null,
                null,
                null,
                null,
                "${DatabaseHandler.TABLE_PLAYER_WINNER_ID} DESC",
                "5"
            )

        } catch (sqlException: SQLException) {
            db.close()
            return result
        }

        var player: PlayerWinner
        if (cursor.moveToFirst()) {
            do {
                player = PlayerWinner("")
                player.id = cursor.getInt(0).toString()
                player.name = cursor.getString(1)
                result.add(player)
            } while (cursor.moveToNext())
        }
        return result
    }


}