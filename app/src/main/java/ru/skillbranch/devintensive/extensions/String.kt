package ru.skillbranch.devintensive.extensions



fun String.truncate(count: Int = 16): String {
    val input = trim()
    return when {
        input.length < count -> input
        else -> "${input.substring(0, if (input[count - 1] == ' ') count - 1 else count)}..."
    }
}

// заменяем все множественные пробелы на одинарный и удаляем все html-теги + html-escape последовательности
fun String.stripHtml(): String = replace( "<.*?>|&amp;|&gt;|&lt;|&quot;|&apos;|&nbsp;".toRegex(), "")
    .replace("\\s+".toRegex(), " ")
