package io.github.sovathna.khmerdictionary

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Mock
  private lateinit var mockContext:Context
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }
}