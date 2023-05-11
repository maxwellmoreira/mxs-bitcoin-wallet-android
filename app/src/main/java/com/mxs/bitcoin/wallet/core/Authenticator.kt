package com.mxs.bitcoin.wallet.core

/**
 * The PIN (personal identification code) is a mandatory password used to access and perform operations in the application.
 * The original PIN value is not used in authentication, but the result of its encryption.
 * This class implements the PIN encryption process.
 */
class Authenticator {

    /**
     *
     */
    fun generateSqlCipherAuthenticator(pin: String): String {
        val resultList = mutableListOf<String>()
        for (i in 1..6) {
            resultList.add(encryptToSqlCipher(pin, i, i % 2 == 0))
        }
        return resultList.sorted().joinToString("")
    }

    /**
     *
     */
    fun generateKeyStoreAuthenticator(pin: String): String {
        val resultList = mutableListOf<String>()
        for (i in 1..6) {
            resultList.add(encryptToKeyStore(pin, i, i % 2 == 0))
        }
        return resultList.sortedDescending().joinToString("")
    }

    /**
     *
     */
    private fun encryptToSqlCipher(pin: String, index: Int, isDescending: Boolean): String {
        val chars = pin.toCharArray()
        val excludedChar = chars[index - 1]
        if (isDescending) {
            return "$excludedChar${
                chars.filterIndexed { i, _ -> i != index - 1 }.sortedDescending().joinToString("")
            }"
        } else {
            return "$excludedChar${
                chars.filterIndexed { i, _ -> i != index - 1 }.sorted().joinToString("")
            }"
        }
    }

    /**
     *
     */
    private fun encryptToKeyStore(pin: String, index: Int, isAscending: Boolean): String {
        val chars = pin.toCharArray()
        val excludedChar = chars[index - 1]
        if (isAscending) {
            return "$excludedChar${
                chars.filterIndexed { i, _ -> i != index - 1 }.sorted().joinToString("")
            }"
        } else {
            return "$excludedChar${
                chars.filterIndexed { i, _ -> i != index - 1 }.sortedDescending().joinToString("")
            }"
        }
    }
}