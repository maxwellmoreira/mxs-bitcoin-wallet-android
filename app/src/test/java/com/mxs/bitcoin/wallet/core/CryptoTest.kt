package com.mxs.bitcoin.wallet.core

import org.junit.Assert.*
import org.junit.Test

class CryptoTest {

    private val seed = Seed()

    @Test
    fun testEncryptAndDecryptSeeds() {
        val pin = "123456"
        val seeds =
            "grass scare swamp any pond purchase repeat dutch catch garbage trigger scene " +
                    "chef spice omit merge illness ankle win equal garbage deliver pond grass"

        val encryptedSeeds = seed.encryptSeeds(seeds, pin)
        val decryptedSeeds = seed.decryptSeeds(encryptedSeeds, pin)

        assertEquals(seeds, decryptedSeeds)
    }
}