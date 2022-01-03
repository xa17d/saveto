package at.xa1.saveto.model

import java.text.CharacterIterator
import java.text.StringCharacterIterator

// Source: https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java
fun humanReadableByteCount(bytes: Long): String {
    if (bytes < 0) {
        error("$bytes must be >0")
    }
    if (bytes < 1024) {
        return "$bytes B"
    }
    var value = bytes
    val prefixes: CharacterIterator = StringCharacterIterator("KMGTPE")
    var i = 40
    while (i >= 0 && value > 0xfffccccccccccccL shr i) {
        value = value shr 10
        prefixes.next()
        i -= 10
    }
    value *= java.lang.Long.signum(bytes).toLong()
    return String.format("%.1f %ciB", value / 1024.0, prefixes.current())
}
