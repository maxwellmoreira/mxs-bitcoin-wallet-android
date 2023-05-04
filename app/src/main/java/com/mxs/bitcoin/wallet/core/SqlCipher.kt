package com.mxs.bitcoin.wallet.core

import android.content.ContentValues
import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

/**
 * The wallet uses SQLCipher which is an extension of SQLite to add encryption using the AES-256 algorithm
 * to the database. That is, not only the records will be encrypted, but also the names of the tables and columns.
 */
class SqlCipher(context: Context, password: String) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        /**
         * here it is necessary to load the native SQLite libraries that will make it possible to
         * perform operations with the database
         */
        SQLiteDatabase.loadLibs(context)
    }

    /**
     *
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "mxs_strongbox_wallet.db"
    }

    /**
     *
     */
    private val password = password

    /**
     * function called when database is first created
     *
     * "PRAGMA key" setting sets a password to decrypt the database
     *
     * "PRAGMA cipher_memory_security" allows you to configure the memory used for data encryption
     * in order to prevent "cold-boot" attacks and other techniques for extracting data from memory
     *
     * "PRAGMA cipher_use_hmac" SQLCipher uses HMAC to check if the data has been corrupted or
     * altered by some external means
     *
     * @param db represents the connection to the database
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS alias(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "iv TEXT(16), salt TEXT(16), dictionary TEXT(216))")
        db?.rawExecSQL("PRAGMA key = $password")
        db?.rawExecSQL("PRAGMA cipher_memory_security = ON;")
        db?.rawExecSQL("PRAGMA cipher_use_hmac = ON;")
        db?.close()
    }

    /**
     *
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS alias")
        onCreate(db)
    }

    /**
     * function responsible for inserting data into the alias table
     *
     * @param iv input value used in conjunction with a cryptographic key to ensure that
     * the same message is not encrypted the same way twice
     * @param salt unique value for each execution of the function, used as one of the parameters
     * in the generation of the cryptographic key.
     * without the use of the salt, an attacker can use dictionary or brute-force attack techniques
     * to guess the encryption key and decrypt the message.
     * @param dictionary data used in the generation of encryption that will be applied to the
     * private key that will be stored in the Keystore.
     */
    fun insertData(iv: String, salt: String, dictionary: String) {
        val db = getWritableDatabase(password)
        val values = ContentValues().apply {
            put("iv", iv)
            put("salt", salt)
            put("dictionary", dictionary)
        }
        db.insert("alias", null, values)
        db.close()
    }

    /**
     * function responsible for getting persisted record from the alias table
     *
     * @return only 1 record should always come
     */
    fun getAllAlias(): List<Alias> {
        val db = getReadableDatabase(password)
        val cursor = db.rawQuery("SELECT * FROM alias", null)
        val aliases = mutableListOf<Alias>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val iv = cursor.getString(cursor.getColumnIndex("iv"))
            val salt = cursor.getString(cursor.getColumnIndex("salt"))
            val dictionary = cursor.getString(cursor.getColumnIndex("dictionary"))

            val alias = Alias(id, iv, salt, dictionary)
            aliases.add(alias)
        }

        cursor.close()
        db.close()
        return aliases
    }

    class Alias(val id: Long, val iv: String, val salt: String, val dictionary: String)
}