package com.mxs.bitcoin.wallet.core

import java.io.FileInputStream

/**
 *
 */
class Random {
    /**
     *
     */
    fun getRandomSequenceFromKernel(): ByteArray {
        val fis = FileInputStream("/dev/random")
        val byteArray = ByteArray(16)
        fis.read(byteArray)
        fis.close()
        return byteArray
    }
}