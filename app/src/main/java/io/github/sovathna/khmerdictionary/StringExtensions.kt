package io.github.sovathna.khmerdictionary

fun String.toKmStringNum(): String {
    var tmp = ""
    forEach {
        tmp += when (it) {
            '1' -> "១"
            '2' -> "២"
            '3' -> "៣"
            '4' -> "៤"
            '5' -> "៥"
            '6' -> "៦"
            '7' -> "៧"
            '8' -> "៨"
            '9' -> "៩"
            '0' -> "០"
            else -> "$it"
        }
    }
    return tmp
}