package ph.stacktrek.novare.snakeandladder.geronimo.jance.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import ph.stacktrek.novare.snakeandladder.geronimo.jance.model.Player

interface PlayerDAO {
    fun addPlayer(player: Player)
    fun getPlayers(): ArrayList<Player>
    fun updatePlayer(player: Player)
    fun deletePlayer(player: Player)
    fun getLastInsertedId(): Long
    fun getPlayerId(player: Player): Int
}

class PlayerDAOSQLLiteImplementation(var context: Context) : PlayerDAO {
    override fun addPlayer(player: Player) {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TABLE_PLAYER_NAME, player.name)

        var status = db.insert(
            DatabaseHandler.TABLE_PLAYER,
            null,
            contentValues
        )
        db.close()
    }

    override fun getPlayers(): ArrayList<Player> {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var result = ArrayList<Player>()
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.TABLE_PLAYER_ID,
            DatabaseHandler.TABLE_PLAYER_NAME
        )

        try {
            cursor = db.query(
                DatabaseHandler.TABLE_PLAYER,
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

        var player: Player
        if (cursor.moveToFirst()) {
            do {
                player = Player("")
                player.name = cursor.getString(1)
                player.id = cursor.getInt(0).toString()
                result.add(player)
            } while (cursor.moveToNext())
        }
        return result
    }

    override fun updatePlayer(player: Player) {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.TABLE_PLAYER_NAME, player.name)

        var status = db.update(
            DatabaseHandler.TABLE_PLAYER,
            contentValues,
            "${DatabaseHandler.TABLE_PLAYER_ID} = ?",
            arrayOf(player.id)
        )
        db.close()
    }

    override fun deletePlayer(player: Player) {
        TODO("Not yet implemented")
    }

    override fun getLastInsertedId(): Long {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase

        val cursor = db.rawQuery("SELECT last_insert_rowid()", null)

        var lastInsertedId: Long = -1
        if (cursor.moveToFirst()) {
            lastInsertedId = cursor.getLong(0)
        }

        cursor.close()
        db.close()

        return lastInsertedId
    }

    override fun getPlayerId(player: Player): Int {
        val databaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null
        val columns = arrayOf(DatabaseHandler.TABLE_PLAYER_ID)
        val selection = "${DatabaseHandler.TABLE_PLAYER_NAME} = ?"
        val selectionArgs = arrayOf(player.name)

        try {
            cursor = db.query(
                DatabaseHandler.TABLE_PLAYER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

        } catch (sqlException: SQLException) {
            db.close()
            return -1
        }

        var playerId = -1
        if (cursor.moveToFirst()) {
            playerId = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return playerId
    }

}