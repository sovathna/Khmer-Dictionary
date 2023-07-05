package io.github.sovathna.khmerdictionary.ui.words

sealed interface WordsType {
  object Words : WordsType
  object Histories : WordsType
  object Bookmarks : WordsType
  data class Searches(val searchTerm: String) : WordsType
}