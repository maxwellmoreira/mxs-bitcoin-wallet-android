package com.mxs.bitcoin.wallet.core

import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.DeterministicSeed
import java.security.SecureRandom

/**
 * class responsible for implementing functions related to the wallet seed
 */
class Seed {

    /**
     * encapsulates information about the Bitcoin network an application is operating on,
     * such as protocol version, address prefixes, communication ports, and other network-specific settings
     */
    private val networkParameters = MainNetParams.get()

    /**
     * function responsible for seed generation
     *
     * @param password password used in seed generation and wallet restoration
     *
     * @return deterministic seed
     */
    fun generateSeed(password: String): DeterministicSeed {
        val entropy = ByteArray(32)
        SecureRandom.getInstanceStrong().nextBytes(entropy)
        val mnemonicList = MnemonicCode().toMnemonic(entropy)
        val timestamp = System.currentTimeMillis() / 1000
        return DeterministicSeed(mnemonicList, null, password, timestamp)
    }

    /**
     * function responsible for restoring a wallet from a list of mnemonics and a password
     *
     * @param mnemonicList keywords that represent the seeds
     * @param password password used to create the wallet
     *
     * @return deterministic seed
     */
    fun restoreSeed(mnemonicList: List<String>, password: String): DeterministicSeed {
        val timestamp = System.currentTimeMillis() / 1000
        return DeterministicSeed(mnemonicList, null, password, timestamp)
    }

    /**
     * function responsible for obtaining the private key of a deterministic wallet
     *
     * @param deterministicSeed deterministic seed
     *
     * @return private key
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