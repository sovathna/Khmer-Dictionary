package io.github.sovathna.khmerdictionary

import io.github.sovathna.khmerdictionary.model.Config

object Const {

    const val CONFIG_URL =
        "https://raw.githubusercontent.com/sovathna/database/main/android/config.json"
    const val DB_URL =
        "https://raw.githubusercontent.com/sovathna/database/main/android/room_sqlite.zip"
    const val PAGE_SIZE = 100
    const val LOAD_MORE_OFFSET = 20
    const val DB_NAME = "dict.db"
    const val LOCAL_DB_NAME = "local.db"

    var config = Config()
}