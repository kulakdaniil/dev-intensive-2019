package ru.skillbranch.devintensive.models.data

import org.w3c.dom.Text
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat (
    val id: String,
    val title: String,
    // По умолчанию - пустая коллекция
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    fun unreadableMessageCount(): Int {
        // Возвращает кол-во непрочитанных сообщений
        return messages.filter { !it.isReaded }.size
    }

    fun lastMessageDate(): Date? =
        if (messages.size > 0)
            messages[messages.size - 1].date
        else null

    fun lastMessageShort(): Pair<String?, String?> =
        when (val message = if (messages.size > 0) messages[messages.size - 1] else null) {
            is ImageMessage -> "${message.from.firstName} - отправил фото" to message.from.firstName
            is TextMessage -> message.text to message.from.firstName
            else -> "" to ""
        }


    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem =
        if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            val messageShort = lastMessageShort()
            ChatItem(
                id,
                null,
                "",
                title,
                messageShort.first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                messageShort.second
            )
        }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}