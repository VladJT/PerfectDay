package jt.projects.perfectday.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.core.viewholders.*

class BaseAdapter(
    private var onEditNoteClicked: ((DataModel) -> Unit)?,
    private var onDeleteClicked: ((Int) -> Unit)?,
    private var onItemClicked: ((DataModel) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val BIRTHDAY_FROM_VK = 1
        const val BIRTHDAY_FROM_PHONE = 2
        const val SIMPLE_NOTICE = 3
        const val HOLIDAY = 4 // сейчас не используется
        const val SCHEDULED_EVENT = 5
        const val UNKNOWN = -1
    }

    private var data: List<DataModel> = arrayListOf()

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
            BIRTHDAY_FROM_VK -> BirthdayFromVKViewHolder(parent)
            SIMPLE_NOTICE -> NoticeHeaderOnlyViewHolder(parent)
            SCHEDULED_EVENT -> ScheduledEventViewHolder(parent)
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BirthdayFromVKViewHolder) {
            holder.bind(data[position], onItemClicked)
        }
        if (holder is BirthdayFromPhoneViewHolder) {
            holder.bind(data[position], onItemClicked)
        }
        if (holder is NoticeHeaderOnlyViewHolder) {
            holder.bind(data[position])
        }
        if (holder is ScheduledEventViewHolder) {
            holder.bind(data[position], onEditNoteClicked, onDeleteClicked)
        }
    }

    override fun getItemCount(): Int = data.size
}