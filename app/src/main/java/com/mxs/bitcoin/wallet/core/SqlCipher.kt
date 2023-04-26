package com.mxs.bitcoin.wallet.core

import android.content.ContentValues
import android.content.Context
import android.util.Log
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import java.io.File

/**
 * Class responsible for operations with SqlCipher.
 * The database is fully encrypted. User PIN is required to access your information.
 */
class SqlCipher(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        val databasePath = context.getDatabasePath(DATABASE_NAME).absolutePath
        Log.d("SqlCipher", "Database path: $databasePath")
    }

    /**
     *
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "mxs_bitcoin_wallet.db"
        private const val DATABASE_PATH = "/data/user/0/com.mxs.bitcoin.wallet/databases/$DATABASE_NAME"
    }

    /**
     *
     */
    private val passwordBytes = SQLiteDatabase.getBytes("password".toCharArray())

    /**
     *
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE my_table (id INTEGER PRIMARY KEY, name TEXT)")
    }

    /**
     *
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS my_table")
        onCreate(db)
    }

    /**
     *
     */
    fun insertData(name: String) {
        val values = ContentValues().apply {
            put("name", name)
        }
        val db = getWritableDatabase(passwordBytes)
        db.insert("my_table", null, values)
        db.close()
    }

    /**
     *
     */
    fun queryData(name: String): List<Pair<Long, String>> {
        val db = getReadableDatabase(passwordBytes)
        val cursor = db.rawQuery("SELECT id, name FROM my_table WHERE name = ?", arrayOf(name))

        val data = mutableListOf<Pair<Long, String>>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            data.add(id to name)
        }

        cursor.close()
        db.close()
        return data
    }

    /**
     * function responsible for checking the existence of the database "mxs_bitcoin_wallet.db"
     *
     * @return if true the database exists
     */
    fun checkDatabaseFileExists(): Boolean {
        val file = File(DATABASE_PATH)
        return file.exists()
    }
}