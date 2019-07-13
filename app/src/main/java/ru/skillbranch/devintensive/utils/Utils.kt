package ru.skillbranch.devintensive.utils

object Utils {
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
    fun transliteration(payload: String, divider: String = " "): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}