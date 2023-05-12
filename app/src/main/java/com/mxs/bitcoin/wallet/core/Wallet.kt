package com.mxs.bitcoin.wallet.core

import android.content.Context
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.DeterministicSeed
import java.util.*

/**
 * class responsible for implementing the wallet's functionalities
 */
class Wallet {

    private val networkParameters: NetworkParameters = MainNetParams.get()

    /**
     *
     */
    fun prepare(context: Context, pin: String, deterministicSeed: DeterministicSeed) {
        val dictionary = Dictionary().generateDictionary()
        val iv = Random().getRandomSequenceFromKernel()
        val salt = Random().getRandomSequenceFromKernel()
        val encryptedPassword = PrivateKey().generateEncryptionPassword(pin, dictionary)
        val privateKey = getPrivateKey(deterministicSeed)
        val encryptedPrivateKey = PrivateKey().encrypt(privateKey, iv, salt, encryptedPassword)
        val encryptedSqlCipherPassword = Authenticator().generateSqlCipherAuthenticator(pin)
        val encryptedKeyStorePassword = Authenticator().generateKeyStoreAuthenticator(pin)
        val sqlCipher = SqlCipher(context, encryptedSqlCipherPassword)
        sqlCipher.insertData(
            iv = Base64.getEncoder().encodeToString(iv),
            salt = Base64.getEncoder().encodeToString(salt),
            dictionary = dictionary.toString()
        )
        val encryptedPrivateKeyStore =
            KeyStore().encrypt(encryptedPrivateKey, "mxs", encryptedKeyStorePassword)
        val decryptedPrivateKeyStore =
            KeyStore().decrypt(encryptedPrivateKeyStore, "mxs", encryptedKeyStorePassword)
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
    }

    /**
     *
     */
    fun getPrivateKey(deterministicSeed: DeterministicSeed): String {
        val wallet = org.bitcoinj.wallet.Wallet.fromSeed(
            networkParameters,
            deterministicSeed,
            Script.ScriptType.P2PKH
        )
        val watchingKey = wallet.watchingKey
        return watchingKey.serializePrivB58(networkParameters)
    }
}