package ru.skillbranch.devintensive.models

class Chat (
    val id: String,
    // По умолчанию - пустая коллекция
    val members: MutableList<User> = mutableListOf(),
    val messages: MutableList<BaseMessage> = mutableListOf()
) {
}