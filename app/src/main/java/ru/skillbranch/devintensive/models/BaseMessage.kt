package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage (
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        // payload - полезная нагрузка, любой класс. Any - это не аналог object из java, т.к. содержит меньше методов
        fun makeMessage(from: User?, chat: Chat, date: Date = Date(), type: String = "text", payload: Any?): BaseMessage {
            return when(type) {
                "image" -> ImageMessage("$lastId", from, chat, date = date, image = payload as String)
                else -> TextMessage("$lastId", from, chat, date = date, text = payload as String)
            }
        }
    }
}