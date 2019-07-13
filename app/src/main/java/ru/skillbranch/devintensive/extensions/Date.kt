package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

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
    TODO("not implemented")
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}