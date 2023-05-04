package com.mxs.bitcoin.wallet.core

import android.content.Context
import java.io.File

/**
 *
 */
class Wallet {

    /**
     *
     */
    fun prepare(context: Context, pin: String) {

        val encryptedSqlCipherPassword = Authenticator().generateSqlCipherAuthenticator(pin)
        val encryptedKeyStorePassword = Authenticator().generateKeyStoreAuthenticator(pin)

        val iv = Random().getRandomSequenceFromKernel()
        val salt = Random().getRandomSequenceFromKernel()
        val dictionary = Dictionary().generateDictionary()

        //------------------------------------------------------------------------------------------

        val sqlCipher = SqlCipher(context, "12345678")
        sqlCipher.insertData(
            iv = iv.decodeToString(),
            salt = salt.decodeToString(),
            dictionary = dictionary.toString()
        )

        val sqlCipher1 = SqlCipher(context, "12345678")
        val alias = sqlCipher1.getAllAlias()
        println("-------------------------> ${alias}")~

        val stringBuilder = StringBuilder()
        for (item in alias) {
            stringBuilder.append("ID: ${item.id}, Dictionary: ${item.dictionary}\n")
        }
        println("-------------------------> $stringBuilder")

        //------------------------------------------------------------------------------------------

        val encryptedPassword = PrivateKey().generateEncryptedPassword(pin, dictionary)
        println("-----------------------> $encryptedPassword")
        val encryptedPrivateKey = PrivateKey().encrypt("teste", iv, salt, encryptedPassword)
        println("-----------------------> $encryptedPrivateKey")
    }

    /**
     * function responsible for checking the existence of the database "mxs_bitcoin_wallet.db"
     *
     * @return if true the database exists
     */
    fun checkDatabaseFileExists(): Boolean {
        val file = File(Persistence.DATABASE_PATH.value)
        return file.exists()
    }
}