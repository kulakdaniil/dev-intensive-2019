package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import java.util.*

abstract class BaseMessage (
    val id: String,
    val from: User,
    val chat: Chat,
    val isIncoming: Boolean = true,
    val date: Date = Date(),
    var isReded: Boolean = false
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        // payload - полезная нагрузка, любой класс. Any - это не аналог object из java, т.к. содержит меньше методов
        fun makeMessage(from: User, chat: Chat, date: Date = Date(), type: String = "text", payload: Any?,
                        isIncoming: Boolean = false): BaseMessage {
            return when(type) {
                "image" -> ImageMessage("$lastId", from, chat, date = date, image = payload as String,
                    isIncoming = isIncoming)
                else -> TextMessage("$lastId", from, chat, date = date, text = payload as String,
                    isIncoming = isIncoming)
            }
        }
    }
}