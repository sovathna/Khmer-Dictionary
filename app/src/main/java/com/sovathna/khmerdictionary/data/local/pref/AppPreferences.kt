package com.sovathna.khmerdictionary.data.local.pref

interface AppPreferences {

  fun setTextSize(textSize: Float)

  fun getTextSize(): Float

  fun incrementTextSize(): Float

  fun decrementTextSize(): Float

}