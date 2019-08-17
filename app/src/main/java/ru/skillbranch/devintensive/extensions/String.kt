package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    val input = trim()
    return when {
        input.length < count -> input
        else -> "${if (input[count - 1] == ' ') input.substring(0, count - 1) else input.substring(0, count)}..."
    }
}
