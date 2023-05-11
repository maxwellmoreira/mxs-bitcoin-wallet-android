package com.mxs.bitcoin.wallet.core

import android.content.Context
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.DeterministicSeed
import java.io.File
import java.util.*

/**
 *
 */
class Wallet {

    private val networkParameters: NetworkParameters = MainNetParams.get()

    /**
     *
     */
    fun prepare(context: Context, pin: String, deterministicSeed: DeterministicSeed?) {
        val dictionary = Dictionary().generateDictionary()
        println("dictionary ----------------> $dictionary")
        val iv = Random().getRandomSequenceFromKernel()
        println("iv ----------------> $iv")
        val salt = Random().getRandomSequenceFromKernel()
        println("salt ----------------> $salt")
        val encryptedPassword = PrivateKey().generateEncryptedPassword(pin, dictionary)
        println("encryptedPassword ----------------> $encryptedPassword")
        //val privateKey = getPrivateKey()
        val encryptedPrivateKey = PrivateKey().encrypt("privateKeyTest", iv, salt, encryptedPassword)
        println("encryptedPrivateKey ----------------> $encryptedPrivateKey")
        val encryptedSqlCipherPassword = Authenticator().generateSqlCipherAuthenticator(pin)
        println("encryptedSqlCipherPassword ----------------> $encryptedSqlCipherPassword")
        val encryptedKeyStorePassword = Authenticator().generateKeyStoreAuthenticator(pin)
        println("encryptedKeyStorePassword -------------------> $encryptedKeyStorePassword")
        val sqlCipher = SqlCipher(context, encryptedSqlCipherPassword)
        sqlCipher.insertData(iv = Base64.getEncoder().encodeToString(iv), salt = Base64.getEncoder().encodeToString(salt), dictionary = dictionary.toString())
        val encryptedPrivateKeyStore = KeyStore().encrypt(encryptedPrivateKey, "mxs", encryptedKeyStorePassword)
        println("encryptedPrivateKeyStore ----------------------> $encryptedPrivateKeyStore")
        val decryptedPrivateKeyStore = KeyStore().decrypt(encryptedPrivateKeyStore, "mxs", encryptedKeyStorePassword)
        println("decryptedPrivateKeyStore ----------------------> $decryptedPrivateKeyStore")
        val sqlCipher1 = SqlCipher(context, encryptedSqlCipherPassword)
        val alias = sqlCipher1.getAllAlias()
        val ivSelect = alias[0].iv
        val saltSelect = alias[0].salt
        val privateKeyOriginal = PrivateKey().decrypt(
            decryptedPrivateKeyStore,
            Base64.getDecoder().decode(ivSelect),
            Base64.getDecoder().decode(saltSelect),
            encryptedPassword
        )
        println("--------------------------> $privateKeyOriginal")
    }

    /**
     *
     */
    private fun getPrivateKey(deterministicSeed: DeterministicSeed): String {
        val wallet = org.bitcoinj.wallet.Wallet.fromSeed(
            networkParameters,
            deterministicSeed,
            Script.ScriptType.P2PKH
        )
        val watchingKey: DeterministicKey = wallet.watchingKey
        return watchingKey.serializePrivB58(networkParameters)
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