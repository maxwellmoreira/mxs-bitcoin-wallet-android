package com.mxs.bitcoin.wallet.core

import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.wallet.DeterministicSeed
import java.security.SecureRandom

/**
 * Class responsible for seed generation
 */
class Seed {
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
}