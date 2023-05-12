package com.mxs.bitcoin.wallet.core

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * KeyStore is a secure repository used to store cryptographic keys, certificates and other
 * secrets related to security in Android applications.
 *
 * In the context of this wallet the KeyStore will be used to store the private keys.
 */
class KeyStore {

    /**
     *
     */
    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
    }

    /**
     * function responsible for storing the private key of the wallet in the KeyStore
     *
     * @param encryptedPrivateKey
     * @param keyAlias
     * @param encryptedKeyStorePassword
     *
     * @return
     */
    fun encrypt(
        encryptedPrivateKey: String,
        keyAlias: String,
        encryptedKeyStorePassword: String
    ): String {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        var secretKey =
            keyStore.getKey(keyAlias, encryptedKeyStorePassword.toCharArray()) as? SecretKey
        if (secretKey == null) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEY_STORE
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            secretKey = keyGenerator.generateKey()
        }
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val cipherText = cipher.doFinal(encryptedPrivateKey.toByteArray())
        val cipherTextBase64 = Base64.encodeToString(cipherText, Base64.DEFAULT)
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
        return "$cipherTextBase64:$ivBase64"
    }

    /**
     * function responsible for exporting the KeyStore private key
     *
     * @param cipherTextBase64
     * @param keyAlias
     * @param encryptedKeyStorePassword
     *
     * @return
     */
    fun decrypt(
        cipherTextBase64: String,
        keyAlias: String,
        encryptedKeyStorePassword: String
    ): String {
        val parts = cipherTextBase64.split(":")
        val cipherText = Base64.decode(parts[0], Base64.DEFAULT)
        val iv = Base64.decode(parts[1], Base64.DEFAULT)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        val secretKey =
            keyStore.getKey(keyAlias, encryptedKeyStorePassword.toCharArray()) as SecretKey
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val plainText = cipher.doFinal(cipherText)
        return String(plainText)
    }
}