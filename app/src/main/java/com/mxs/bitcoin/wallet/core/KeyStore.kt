package com.mxs.bitcoin.wallet.core

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 *
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
     *
     */
    fun encrypt(plainText: String, keyAlias: String): String {

        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
        var secretKey = keyStore.getKey(keyAlias, null) as? SecretKey
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
        val cipherText = cipher.doFinal(plainText.toByteArray())

        val cipherTextBase64 = Base64.encodeToString(cipherText, Base64.DEFAULT)
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
        return "$cipherTextBase64:$ivBase64"
    }

    /**
     *
     */
    fun decrypt(cipherTextBase64: String, keyAlias: String): String {
        val parts = cipherTextBase64.split(":")
        val cipherText = Base64.decode(parts[0], Base64.DEFAULT)
        val iv = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val plainText = cipher.doFinal(cipherText)

        return String(plainText)
    }
}