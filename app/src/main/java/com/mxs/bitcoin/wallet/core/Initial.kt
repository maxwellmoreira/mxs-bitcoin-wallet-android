package com.mxs.bitcoin.wallet.core

import android.annotation.SuppressLint
import java.io.File

/**
 * class responsible for implementing the checks that occur when starting the application
 */
class Initial {

    companion object {
        private const val DATABASE_NAME = "mxs_strongbox_wallet.db"
        @SuppressLint("SdCardPath")
        private const val DATABASE_PATH = "/data/user/0/com.mxs.bitcoin.wallet/databases/$DATABASE_NAME"
    }

    /**
     * function responsible for checking the existence of the database "mxs_strongbox_wallet.db"
     *
     * @return if true the database exists
     */
    fun checkDatabaseFileExists(): Boolean {
        val file = File(DATABASE_PATH)
        return file.exists()
    }
}