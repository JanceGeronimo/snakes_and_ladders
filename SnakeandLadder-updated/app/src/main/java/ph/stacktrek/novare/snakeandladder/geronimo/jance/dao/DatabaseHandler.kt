package ph.stacktrek.novare.snakeandladder.geronimo.jance.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASEVERSION) {

    companion object {
        private val DATABASEVERSION = 1
        private val DATABASENAME = "PlayersDatabase"

        const val TABLE_PLAYER = "player_table"
        const val TABLE_PLAYER_ID = "id"
        const val TABLE_PLAYER_NAME = "name"
        const val TABLE_PLAYER_COLOR = "color"

        const val TABLE_PLAYER_WINNER = "player_winner_table"
        const val TABLE_PLAYER_WINNER_ID = "id"
        const val TABLE_PLAYER_WINNER_NAME = "name"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PLAYERS_TABLE =
            "CREATE TABLE $TABLE_PLAYER " +
                    "($TABLE_PLAYER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$TABLE_PLAYER_NAME TEXT, " +
                    "$TABLE_PLAYER_COLOR TEXT)"

        db?.execSQL(CREATE_PLAYERS_TABLE)

        //Set first default values
//        db?.execSQL("Insert into $TABLE_PLAYER ($TABLE_PLAYER_NAME) values ('Jane')")
        db?.execSQL("Insert into $TABLE_PLAYER ($TABLE_PLAYER_NAME) values ('John')")
        db?.execSQL("Insert into $TABLE_PLAYER ($TABLE_PLAYER_NAME) values ('Mary')")

        val CREATE_PLAYERS_WINNER_TABLE =
            "CREATE TABLE $TABLE_PLAYER_WINNER " +
                    "($TABLE_PLAYER_WINNER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$TABLE_PLAYER_WINNER_NAME TEXT)"

        db?.execSQL(CREATE_PLAYERS_WINNER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYER")
        onCreate(db)
    }


}