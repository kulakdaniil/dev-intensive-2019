package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.GroupRepository

class GroupViewModel : ViewModel() {

    private val query = mutableLiveData("")
    private val groupRepository = GroupRepository
    private val userItems = mutableLiveData(loadUsers())
    private val selectedItems = Transformations.map(userItems){ users -> users.filter { it.isSelected } }

    fun getUsersData(): LiveData<List<UserItem>> {
        val result = MediatorLiveData<List<UserItem>>()
        // Внутренняя локальная лямбда
        // аргументы ей не нужны, т.к. работаем с пропертями класса
        val filterF = {
            val queryStr = query.value!!
            val users = userItems.value!!

            result.value = if (queryStr.isEmpty()) users
            else users.filter { it.fullName.contains(queryStr, true) }
        }
        // MediatorLiveData - класс, который на вход может получать большое кол-во источников
        // которое можно ему передать и он может подписаться на их изменение
        // Допустим, у нас есть 2, 3, 4 LiveData, которые могут у нас изменяться
        // и вам нужно одно результирующее значение, которое будет обрабатывать изменение каждой из LiveData
        // Нам нужно обрабатывать изменение query-запроса и изменение наших userItems (те, которые были выбраны)
        // Первый аргумент - на какие данные необходимо подписаться медиатором LiveData, а вторым аргументом
        // передаем туда лямбду, которая будет вызываться каждый раз, когда эти данные будут изменены
        // по сути это метод onChange
        result.addSource(userItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    fun handleSelectedItem(userId: String) {
        userItems.value = userItems.value!!.map{
            if (it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }
    }

    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map{
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }

    private fun loadUsers(): List<UserItem> = groupRepository.loadUsers().map{ it.toUserItem() }

    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }
}