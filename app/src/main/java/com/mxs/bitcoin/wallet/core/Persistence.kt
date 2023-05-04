package com.mxs.bitcoin.wallet.core

enum class Persistence(val value: String) {
    DATABASE_NAME("mxs_strongbox_wallet.db"),
    DATABASE_VERSION("1"),
    DATABASE_PATH("/data/user/0/com.mxs.bitcoin.wallet/databases/$DATABASE_NAME")
}