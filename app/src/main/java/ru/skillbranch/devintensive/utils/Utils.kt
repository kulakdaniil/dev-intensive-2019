package ru.skillbranch.devintensive.utils

object Utils {

    private val transMap = mapOf(
        "а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d",
        "е" to "e", "ё" to "e", "ж" to "zh", "з" to "z", "и" to "i",
        "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n",
        "о" to "o", "п" to "p", "р" to "r", "с" to "s", "т" to "t",
        "у" to "u", "ф" to "f", "х" to "h", "ц" to "c", "ч" to "ch",
        "ш" to "sh", "щ" to "sh'", "ъ" to "", "ы" to "i", "ь" to "",
        "э" to "e", "ю" to "yu", "я" to "ya",
        "А" to "A", "Б" to "B", "В" to "V", "Г" to "G", "Д" to "D",
        "Е" to "E", "Ё" to "E", "Ж" to "Zh", "З" to "Z", "И" to "I",
        "Й" to "I", "К" to "K", "Л" to "L", "М" to "M", "Н" to "N",
        "О" to "O", "П" to "P", "Р" to "R", "С" to "S", "Т" to "T",
        "У" to "U", "Ф" to "F", "Х" to "H", "Ц" to "C", "Ч" to "Ch",
        "Ш" to "Sh", "Щ" to "Sh'", "Ъ" to "", "Ы" to "I", "Ь" to "",
        "Э" to "E", "Ю" to "Yu", "Я" to "Ya"
    )

    fun parseFullName(fullName: String?) : Pair<String?, String?> {
        val parts : List<String>? = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)
//        return Pair(firstName, lastName)
        // Короткая инфиксная запись
        if (firstName != null && firstName.isEmpty()) firstName = null
        if (lastName != null && lastName.isEmpty()) lastName = null
        return firstName to lastName
    }

    // строка для транслитерации и разделитель
    fun transliteration(payload: String, divider: String = " "): String =
        payload.trim()
            //формируем список слов, разбивая строку на пробелы
            .splitToSequence(" ")
            .filter { it.isNotEmpty() }
            .map {
                it.toCharArray()
                //побуквенно ищем в мапе перевод и снова собираем в слово
                .map { ch -> with(transMap[ch.toString()]) { this ?: ch.toString() } }
                    .toList()
                    .joinToString("")
            }
            .toList()
            .joinToString(divider)

    fun toInitials(firstName: String?, lastName: String?): String? =
        with(checkNames(firstName) to checkNames(lastName)) {
            when {
                first == null && second == null -> null
                first != null && second == null -> first
                first == null && second != null -> second
                else -> "$first$second"
            }
        }

    private fun checkNames(name: String?): String? = when (name) {
        "", " ", null -> null
        else -> name[0].toString().toUpperCase()
    }
}