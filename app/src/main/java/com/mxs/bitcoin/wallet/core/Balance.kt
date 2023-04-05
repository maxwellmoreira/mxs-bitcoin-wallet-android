package com.mxs.bitcoin.wallet.core

import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet

class Balance {
    fun viewBalance() {
        val seedCode = "grass scare swamp any pond purchase repeat dutch catch garbage trigger scene chef spice omit merge illness ankle win equal topic deliver input machine"
        val passphrase = "password"
        val networkParameters: NetworkParameters = MainNetParams.get()
        val seed = DeterministicSeed(seedCode, null, passphrase, 0)
        val wallet = Wallet.fromSeed(networkParameters, seed, Script.ScriptType.P2PKH)
        println("Your wallet balance is ${wallet.balance}")
    }
}