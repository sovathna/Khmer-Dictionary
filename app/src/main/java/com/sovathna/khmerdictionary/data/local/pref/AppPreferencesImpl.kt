package com.sovathna.khmerdictionary.data.local.pref

import android.content.SharedPreferences

class AppPreferencesImpl(
  private val pref: SharedPreferences
) : AppPreferences {
  private val editor = pref.edit()

  companion object {
    const val MIN_TEXT_SIZE = 14.0F
    const val MAX_TEXT_SIZE = 40.0F

    private const val TEXT_SIZE = "text_size"
  }

  override fun setTextSize(textSize: Float) {
    editor.putFloat(TEXT_SIZE, textSize).apply()
  }

  override fun getTextSize(): Float {
    return pref.getFloat(TEXT_SIZE, MIN_TEXT_SIZE)
  }

  override fun incrementTextSize(): Float {
    var textSize = getTextSize()
    if (textSize < MAX_TEXT_SIZE) {
      textSize += 2
      setTextSize(textSize)
    }
    return textSize
  }

  override fun decrementTextSize(): Float {
    var textSize = getTextSize()
    if (textSize > MIN_TEXT_SIZE) {
      textSize -= 2
      setTextSize(textSize)
    }
    return textSize
  }
}