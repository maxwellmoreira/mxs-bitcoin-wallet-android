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
    fun generateSeed(password: String): DeterministicSeed {
        val entropy = ByteArray(32)
        SecureRandom.getInstanceStrong().nextBytes(entropy)
        val mnemonicList = MnemonicCode().toMnemonic(entropy)
        val timestamp = System.currentTimeMillis() / 1000
        return DeterministicSeed(mnemonicList, null, password, timestamp)
    }

    fun restoreSeed(mnemonicList: List<String>, password: String): DeterministicSeed {
        val timestamp = System.currentTimeMillis() / 1000
        return DeterministicSeed(mnemonicList, null, password, timestamp)
    }
}