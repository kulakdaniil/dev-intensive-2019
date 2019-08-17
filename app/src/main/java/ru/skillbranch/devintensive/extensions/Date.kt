package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

private val UNITS_MAP = mapOf(
    SECOND to Triple("секунду", "секунды", "секунд"),
    MINUTE to Triple("минуту", "минуты", "минут"),
    HOUR to Triple("час", "часа", "часов"),
    DAY to Triple("день", "дня", "дней")
)

// extension function
// я так понимаю, мы переопределили ф-ию format для всех объектов Date
// Такая ф-ия генерирует статическую ф-ию, которую можно применить к
// экземпляру определенного класса
fun Date.format(pattern:String="HH:mm:ss dd.MM.yy"):String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

// Добавляем к объекту Data определенный временной сдвиг, который будет задаваться задаваемымми нами величинами
// Такого метода нет у объекта Date
fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND) : Date {
    // мс c 1.01.1970
    var time = this.time
    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    // Возвращает значение разницы между временем текущего экземпляра и
    // временем, которое передано в качестве аргумента в человеческом представлении
    // Последний раз был несколько секунд назад - пример
    // Последний раз был четыре дня назад - пример
    // Последний раз был более года назад - пример
    // Не забываем про склонение числительных!!! 4 дня - 7 дней
    /*
        Реализуй extension Date.humanizeDiff(date) (значение по умолчанию текущий момент времени)
        для форматирования вывода разницы между датами в человекообразном формате, с учетом склонения
        числительных. Временные интервалы преобразований к человекообразному формату доступны в ресурсах
        к заданию
        Пример:
        Date().add(-2, TimeUnits.HOUR).humanizeDiff() //2 часа назад
        Date().add(-5, TimeUnits.DAY).humanizeDiff() //5 дней назад
        Date().add(2, TimeUnits.MINUTE).humanizeDiff() //через 2 минуты
        Date().add(7, TimeUnits.DAY).humanizeDiff() //через 7 дней
        Date().add(-400, TimeUnits.DAY).humanizeDiff() //более года назад
        Date().add(400, TimeUnits.DAY).humanizeDiff() //более чем через год
    */
    val future = this.time - date.time > 0
    val diffSec = with (abs(this.time - date.time) / 1000) { if (future) this + 1 else this}

    /*
        0с - 1с "только что"
        1с - 45с "несколько секунд назад"
        45с - 75с "минуту назад"
        75с - 45мин "N минут назад"
        45мин - 75мин "час назад"
        75мин - 22ч "N часов назад"
        22ч - 26ч "день назад"
        26ч - 360д "N дней назад"
        >360д "более года назад"
     */

    var result = when(diffSec) {
        in 0..1 -> "только что"
        in 1..45 -> "${if (future) "через " else "" }несколько секунд${if (!future) " назад" else "" }"
        in 45..75 -> getPastOrFuturePhrase(future, MINUTE)
        in 75..45 * 60 -> getPastOrFuturePhrase(future, MINUTE, diffSec / 60)
        in 60 * 45..60 * 75 -> getPastOrFuturePhrase(future, HOUR)
        in 60 * 75..60 * 60 * 22 -> getPastOrFuturePhrase(future, HOUR, diffSec / (60 * 60))
        in 60 * 60 * 22..60 * 60 * 26 -> getPastOrFuturePhrase(future, DAY)
        in 60 * 60 * 26..60 * 60 * 24 * 360 -> getPastOrFuturePhrase(future, DAY, diffSec / (60 * 60 * 24))
        else -> if (future) "более чем через год" else "более года назад"
    }

    return result
}

private fun getTimeUnit(count: Long, type: Long): String? {
    var newCount = count
    if (newCount > 100) newCount %= 100
    if (newCount > 20) newCount %= 10
    return when (newCount) {
        1L -> UNITS_MAP[type]?.first
        2L, 3L, 4L -> UNITS_MAP[type]?.second
        else -> UNITS_MAP[type]?.third
    }
}

private fun getPastOrFuturePhrase(future: Boolean, type: Long, count: Long = 1L): String =
    "${if (future) "через " else "" }${if (count > 1L) "$count " else "" }" +
            "${getTimeUnit(count, type)}${if (!future) " назад" else "" }"

enum class TimeUnits(private val type: Long) {
    SECOND(ru.skillbranch.devintensive.extensions.SECOND),
    MINUTE(ru.skillbranch.devintensive.extensions.MINUTE),
    HOUR(ru.skillbranch.devintensive.extensions.HOUR),
    DAY(ru.skillbranch.devintensive.extensions.DAY);

    fun plural(value: Int): String = "$value ${getTimeUnit(value.toLong(), this.type)}"
}