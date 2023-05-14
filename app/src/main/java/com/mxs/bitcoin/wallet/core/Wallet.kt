package com.mxs.bitcoin.wallet.core

import android.content.Context
import org.bitcoinj.wallet.DeterministicSeed
import java.util.*

/**
 * class responsible for implementing the wallet's functionalities
 */
class Wallet {

    /**
     * function responsible for saving the private key in the KeyStore
     *
     * @param context provides information about the running application, such as the installation
     * directory, application package, permissions granted, package information, and other metadata
     * @param pin code used to access the wallet
     * @param deterministicSeed represents a deterministic seed in the generation of keys
     * in Bitcoin wallets
     */
    fun importPrivateKey(context: Context, pin: String, deterministicSeed: DeterministicSeed) {
        val dictionary = Dictionary().generateDictionary()
        val privateKey = Seed().getPrivateKey(deterministicSeed)
        val iv = Random().getRandomSequenceFromKernel()
        val salt = Random().getRandomSequenceFromKernel()
        val encryptedPassword = PrivateKey().generateEncryptionPassword(pin, dictionary)
        val encryptedPrivateKey = PrivateKey().encrypt(privateKey, iv, salt, encryptedPassword)
        val encryptedKeyStorePassword = Authenticator().generateKeyStoreAuthenticator(pin)
        val encryptedKeyStore =
            KeyStore().encrypt(encryptedPrivateKey, "mxs", encryptedKeyStorePassword)
        val encryptedSqlCipherPassword = Authenticator().generateSqlCipherAuthenticator(pin)
        val sqlCipher = SqlCipher(context, encryptedSqlCipherPassword)
        sqlCipher.insertData(
            dictionary = dictionary.toString(),
            iv = Base64.getEncoder().encodeToString(iv),
            salt = Base64.getEncoder().encodeToString(salt),
            encryptedKeyStore
        )
    }

    /**
     * function responsible for exporting the private key of the wallet that is stored in the KeyStore
     *
     * @param context provides information about the running application, such as the installation
     * directory, application package, permissions granted, package information, and other metadata
     * @param pin code used to access the wallet
     *
     * @return private key
     */
    fun exportPrivateKey(context: Context, pin: String): String {
        val encryptedSqlCipherPassword = Authenticator().generateSqlCipherAuthenticator(pin)
        val sqlCipher = SqlCipher(context, encryptedSqlCipherPassword)
        val alias = sqlCipher.getAllAlias()
        val dictionary = alias[0].dictionary
        val ks = alias[0].ks
        val iv = alias[0].iv
        val salt = alias[0].salt

        val dictionary1 = dictionary.trim().substring(1, dictionary.length - 1)
        val dictionary2 = dictionary1.split(",").map { it.trim() }

        val encryptedKeyStorePassword = Authenticator().generateKeyStoreAuthenticator(pin)
        val decryptedPrivateKeyStore = KeyStore().decrypt(ks, "mxs", encryptedKeyStorePassword)
        val encryptedPassword = PrivateKey().generateEncryptionPassword(pin, dictionary2)

        return PrivateKey().decrypt(
            decryptedPrivateKeyStore,
            Base64.getDecoder().decode(iv),
            Base64.getDecoder().decode(salt),
            encryptedPassword
        )
    }
}