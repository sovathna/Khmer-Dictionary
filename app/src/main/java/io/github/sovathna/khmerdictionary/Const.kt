package io.github.sovathna.khmerdictionary

import io.github.sovathna.khmerdictionary.model.Config

object Const {

    const val CONFIG_URL =
        "https://raw.githubusercontent.com/sovathna/database/main/android/config.json"
    const val DB_URL = "https://raw.githubusercontent.com/sovathna/database/main/android/room_sqlite.zip"
    const val PAGE_SIZE = 100
    const val DB_NAME = "dict.db"

    var config = Config()
}