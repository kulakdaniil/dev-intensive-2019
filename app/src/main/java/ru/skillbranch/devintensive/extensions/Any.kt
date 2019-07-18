package ru.skillbranch.devintensive.extensions

/**
 *
 * https://stackoverflow.com/questions/45841067/what-is-the-best-way-to-define-log-tag-constant-in-kotlin
 * usage - Log.e(LOG_TAG, "some value")
 * M_ - префикс для удобства фильтрация в logcat - необязателен
 */
val Any.LOG_TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) "M_$tag" else "M_${tag.substring(0, 23)}"
    }