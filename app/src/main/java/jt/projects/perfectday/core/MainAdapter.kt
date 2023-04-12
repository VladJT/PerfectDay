package jt.projects.perfectday.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel

class MainAdapter(
    private var onListItemClick: ((DataModel) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onDeleteClicked: (Int) -> Unit = {
        notifyItemRemoved(it)
    }

    companion object {
        const val BIRTHDAY_FROM_VK = 1
        const val BIRTHDAY_FROM_PHONE = 2
        const val SIMPLE_NOTICE = 3
        const val HOLIDAY = 4
        const val SCHEDULED_EVENT = 5
        const val UNKNOWN = -1
    }

    private var data: List<DataModel> = arrayListOf()

    // Передаём событие во фрагмент
    private fun listItemClicked(listItemData: DataModel) {
        onListItemClick?.let { it(listItemData) }
    }

    fun setData(newData: List<DataModel>) {
        this.data = newData
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is DataModel.BirthdayFromVk -> BIRTHDAY_FROM_VK
            is DataModel.BirthdayFromPhone -> BIRTHDAY_FROM_PHONE
            is DataModel.SimpleNotice -> SIMPLE_NOTICE
            is DataModel.Holiday -> HOLIDAY
            is DataModel.ScheduledEvent -> SCHEDULED_EVENT
            else -> UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            BIRTHDAY_FROM_PHONE -> BirthdayFromPhoneViewHolder(parent)
            SIMPLE_NOTICE -> NoticeViewHolder(parent)
            SCHEDULED_EVENT -> ScheduledEventViewHolder(parent)
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BirthdayFromPhoneViewHolder) {
            onListItemClick?.let { holder.bind(data[position], it) }
        }
        if (holder is NoticeViewHolder) {
            holder.bind(data[position])
        }
        if (holder is ScheduledEventViewHolder) {
            onListItemClick?.let { holder.bind(data[position], it, onDeleteClicked) }
        }
    }

    override fun getItemCount(): Int = data.size
}