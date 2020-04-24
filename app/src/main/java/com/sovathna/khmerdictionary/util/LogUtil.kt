package com.sovathna.khmerdictionary.util

import timber.log.Timber

object LogUtil {
  fun v(message: String) = Timber.v(message)
  fun d(message: String) = Timber.d(message)
  fun i(message: String) = Timber.i(message)
  fun w(message: String) = Timber.w(message)
  fun wtf(message: String) = Timber.wtf(message)

  fun v(throwable: Throwable) = Timber.v(throwable)
  fun d(throwable: Throwable) = Timber.d(throwable)
  fun i(throwable: Throwable) = Timber.i(throwable)
  fun w(throwable: Throwable) = Timber.w(throwable)
  fun wtf(throwable: Throwable) = Timber.wtf(throwable)
}