package com.sovathna.khmerdictionary

object Const {
  var mainTextSize: Float = 14F
  const val PAGE_SIZE = 100
  const val DB_NAME = "kh_kh.db"
  const val DB_TMP_NAME = "tmp_kh_kh.db"
  const val RAW_DB_URL =
    "https://github.com/sovathna/Khmer-Dictionary/raw/master/db/room_sqlite.zip"

  const val LOAD_MORE_THRESHOLD = 30

  const val WORD_LIST_FRAGMENT_TAG = "word_list_fragment_tag"
  const val SEARCH_WORDS_FRAGMENT_TAG = "search_words_fragment_tag"
  const val HISTORIES_FRAGMENT_TAG = "histories_fragment_tag"
  const val BOOKMARKS_FRAGMENT_TAG = "bookmarks_fragment_tag"
  const val DEFINITION_FRAGMENT_TAG = "definition_fragment_tag"
}