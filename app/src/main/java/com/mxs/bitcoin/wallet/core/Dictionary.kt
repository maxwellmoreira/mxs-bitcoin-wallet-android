package com.mxs.bitcoin.wallet.core

import java.security.SecureRandom

/**
 * the dictionary will be stored in SqlCipher.
 * Its function is to help generate the password that will encrypt the seed.
 */
class Dictionary {

    /**
     * function responsible for generating the dictionary
     *
     * @return list containing 216 items, each item with 1 character that can be a letter
     * or a number from 0 to 9.
     */
    fun generateDictionary(): List<String> {
        val random = SecureRandom()
        val alphabet = ('A'..'Z').toList() + ('0'..'9').toList()
        val result = mutableListOf<String>()

        repeat(216) {
            val randomIndex = random.nextInt(alphabet.size)
            result.add(alphabet[randomIndex].toString())
        }
        return result
    }

    /**
     *
     */
    fun getDictionaryFragment(pinDigit: Int, list: List<String>): String {
        return when (pinDigit) {
            1 -> list.subList(0, 4).joinToString("")
            2 -> list.subList(4, 8).joinToString("")
            3 -> list.subList(8, 12).joinToString("")
            4 -> list.subList(12, 16).joinToString("")
            5 -> list.subList(16, 20).joinToString("")
            6 -> list.subList(20, 24).joinToString("")
            7 -> list.subList(24, 28).joinToString("")
            8 -> list.subList(28, 32).joinToString("")
            9 -> list.subList(32, 36).joinToString("")
            else -> throw IllegalArgumentException("Invalid parameter value")
        }
    }
}