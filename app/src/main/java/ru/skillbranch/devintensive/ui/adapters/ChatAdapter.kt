package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.LOG_TAG
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.ui.custom.AvatarImageView

class ChatAdapter(val listener: (ChatItem) -> Unit) : RecyclerView.Adapter<ChatAdapter.SingleViewHolder>() {

    var items: List<ChatItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        // из нашей xml-разметки раздуваем нашу view
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_chat_single, parent, false)
        Log.d(LOG_TAG, "onCreateViewHolder")
        return SingleViewHolder(convertView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder $position")
        holder.bind(items[position], listener)
    }

    fun updateData(data: List<ChatItem>) {

        Log.d(LOG_TAG, "update data adapter - new data ${data.size} hash: ${data.hashCode()}" +
                "old data ${items.size} hash: ${items.hashCode()}")

        val diffCalback = object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                // Для сверки контента, т.к. используем data-классы можем сравнить только хэш-коды
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCalback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class SingleViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer, ItemTouchViewHolder {
        // нет переменной containerView - переопределяем геттер
        override val containerView: View?
            get() = itemView

        /*val iv_avatar = convertView.findViewById<AvatarImageView>(R.id.iv_avatar_single)
        val tv_title = convertView.findViewById<TextView>(R.id.tv_title_single)*/

        fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            // Когда обращаемся через itemView без реализации LayoutContainer - containerView
            // - убиваем наглухо всю производительность
            /*iv_avatar.setInitials(item.initials)
            tv_title.text = item.shortDescription*/

            if (item.avatar == null) {
                // а здесь нужно включать experimental в build.gradle app
                iv_avatar_single.setInitials(item.initials)
            } else {
                // TODO: set drawable
            }

            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE

            with(tv_date_single) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_single) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription
            itemView.setOnClickListener{
                // invoke - вызвать лямбду
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

}