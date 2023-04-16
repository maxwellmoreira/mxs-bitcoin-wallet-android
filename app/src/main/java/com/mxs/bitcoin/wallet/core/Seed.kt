package com.mxs.bitcoin.wallet.core

import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.wallet.DeterministicSeed
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Class responsible for implementing cryptography-related functions during the seeds manipulation process
 */
class Seed {
    /**
     * AES_MODE standard mode that works with the AES encryption algorithm
     * SHA_256 hashing used to generate the encryption key
     */
    companion object {
        private const val AES_MODE = "AES/CBC/PKCS5Padding"
        private const val SHA_256 = "SHA-256"
    }

    /**
     * Method responsible for generating a 256-bit deterministic seed for a Bitcoin wallet.
     */
    fun generateSeed() {
        val passphrase = "password"
        val entropy = ByteArray(32)
        SecureRandom().nextBytes(entropy)
        val seedWords = MnemonicCode().toMnemonic(entropy)
        val seed = DeterministicSeed(seedWords, null, passphrase, 0)
        println("seedWords: ${seedWords}")
        println("Palavras-chave: ${seed.mnemonicCode}")
    }

    /**
     * Function responsible for encrypting the access seeds for the wallet using the AES symmetric
     * encryption algorithm with the key derived from the user's security code,
     * using CBC mode and PKCS5 padding
     *
     * @param seeds encrypted seeds that correspond to the private access key for the wallet
     * @param pin in-app user security code
     * @return array of bytes containing the cryptographic hash of the seeds
     */
    fun encryptSeeds(seeds: String, pin: String): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(cipher.blockSize)
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(pin), ivParameterSpec)
        return cipher.doFinal(seeds.toByteArray())
    }

    /**
     * Function responsible for decrypting the wallet access seeds
     *
     * @param encryptedSeeds array of bytes containing the cryptographic hash of the seeds
     * @param pin in-app user security code
     * @return encrypted seeds that correspond to the private access key for the wallet
     */
    fun decryptSeeds(encryptedSeeds: ByteArray, pin: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(cipher.blockSize)
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, generateKey(pin), ivParameterSpec)
        return String(cipher.doFinal(encryptedSeeds))
    }

    /**
     * Function responsible for generating a cryptographic hash of type SHA-256 based on the pin
     * provided by the user
     *
     * @param pin in-app user security code
     * @return byte array containing the cryptographic hash
     */
    private fun generateKey(pin: String): SecretKeySpec {
        val sha = MessageDigest.getInstance(SHA_256)
        sha.update(pin.toByteArray())
        return SecretKeySpec(sha.digest(), "AES")
    }
}