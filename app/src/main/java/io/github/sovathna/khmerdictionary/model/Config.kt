package io.github.sovathna.khmerdictionary.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Config(
  val version: Int = 1
)