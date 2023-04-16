package com.mxs.bitcoin.wallet.core

import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.params.HKDFParameters
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 *
 */
class KeyStore {

    /**
     *
     */
    fun setEntryKeyStore() {
        val keySet = generateSecretKeyFromSHA256("minha_senha")
        val secretKeySet = KeyStore.SecretKeyEntry(keySet)

        println("Secret Key Binary : " + String(secretKeySet.secretKey.encoded, charset("UTF-8")))
        println("Secret Key to HexString : " + secretKeySet.secretKey.encoded.toHexString())
        val secretKeySetBase64 = Base64.getEncoder().encodeToString(secretKeySet.secretKey.encoded)
        println("Secret Key to Base64 : $secretKeySetBase64")

        val password: CharArray = "minha_senha".toCharArray()
        val protectionParameterSet: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(password)

        val androidCAStore = KeyStore.getInstance(KeyStore.getDefaultType())
        androidCAStore.load(null)
        androidCAStore.setEntry("KEY_ALIAS", secretKeySet, protectionParameterSet)
    }

    /**
     *
     */
    fun getEntryKeyStore() {

        val androidCAStore = KeyStore.getInstance(KeyStore.getDefaultType())
        androidCAStore.load(null)

        val protectionParameterGet: KeyStore.ProtectionParameter = KeyStore.PasswordProtection("minha_senha".toCharArray())

        val keyStoreEntry = androidCAStore.getEntry("KEY_ALIAS", protectionParameterGet) as KeyStore.SecretKeyEntry
        println("Secret Key Binary : " + String(keyStoreEntry.secretKey.encoded, charset("UTF-8")))
        println("Secret Key to HexString : " + keyStoreEntry.secretKey.encoded.toHexString())
        val base64Key = Base64.getEncoder().encodeToString(keyStoreEntry.secretKey.encoded)
        println("Secret Key to Base64 : $base64Key")
    }



    private fun ByteArray.toHexString(): String {
        return joinToString("") { byte -> "%02x".format(byte) }
    }

    fun generateSecretKeyFromSHA256(stringToHash: String): SecretKey {
        val stringBytes = stringToHash.toByteArray(StandardCharsets.UTF_8)
        val sha256Digest: Digest = SHA256Digest()
        sha256Digest.update(stringBytes, 0, stringBytes.size)
        val hashResult = ByteArray(sha256Digest.digestSize)
        sha256Digest.doFinal(hashResult, 0)

        val hkdfGenerator = HKDFBytesGenerator(sha256Digest)
        hkdfGenerator.init(HKDFParameters(hashResult, null, null))
        val keyBytes = ByteArray(16)
        hkdfGenerator.generateBytes(keyBytes, 0, keyBytes.size)

        return SecretKeySpec(keyBytes, "AES")
    }
}