package com.mxs.bitcoin.wallet.core

import java.security.MessageDigest

/**
 *
 */
class Hash {
    /**
     *
     */
    fun convertToSha256(encryptedPin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(encryptedPin.toByteArray())
        return hash.joinToString("") {
            String.format("%02x", it)
        }
    }
}