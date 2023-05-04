package com.mxs.bitcoin.wallet.core

import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.wallet.DeterministicSeed
import java.security.SecureRandom

/**
 *
 */
class Seed {
    /**
     * function responsible for seed generation
     *
     * @param password password used in seed generation and wallet restoration
     * @return list of mnemonic words that represent seeds
     */
    fun generateSeed(password: String): List<String> {
        val entropy = ByteArray(32)
        SecureRandom.getInstanceStrong().nextBytes(entropy)
        val seedWords = MnemonicCode().toMnemonic(entropy)
        val timestamp = System.currentTimeMillis() / 1000
        val seed = DeterministicSeed(seedWords, null, password, timestamp)
        return seed.mnemonicCode!!
    }
}