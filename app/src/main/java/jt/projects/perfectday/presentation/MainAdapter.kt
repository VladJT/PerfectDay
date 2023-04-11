package jt.projects.perfectday.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ItemBirthdayFromPhoneBinding
import jt.projects.perfectday.databinding.ItemScheduledEventBinding
import jt.projects.perfectday.databinding.ItemSimpleNoticeBinding
import jt.projects.utils.toStdFormatString
import jt.projects.utils.ui.CoilImageLoader
import org.koin.java.KoinJavaComponent.getKoin


class MainAdapter(
    private var onListItemClick: ((DataModel) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            BIRTHDAY_FROM_PHONE -> {
                val binding = ItemBirthdayFromPhoneBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BirthdayFromPhoneViewHolder(binding)
            }
            SIMPLE_NOTICE -> {
                val binding = ItemSimpleNoticeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SimpleNoticeViewHolder(binding)
            }
            SCHEDULED_EVENT -> {
                val binding = ItemScheduledEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ScheduledEventViewHolder(binding)
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BirthdayFromPhoneViewHolder) {
            holder.bind(data[position])
        }
        if (holder is SimpleNoticeViewHolder) {
            holder.bind(data[position])
        }
        if (holder is ScheduledEventViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size


    inner class BirthdayFromPhoneViewHolder(private val binding: ItemBirthdayFromPhoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModel: DataModel) {
            val data = dataModel as DataModel.BirthdayFromPhone
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(binding) {
                    tvName.text = data.name
                    tvBirthday.text = data.birthDate.toString()
                    tvAge.text = data.age.toString()

                    getKoin().get<CoilImageLoader>()
                        .loadToCircleView(ivAvatar, R.drawable.default_avatar)
                    root.setOnClickListener { listItemClicked(data) }
                }
            }
        }
    }

    inner class SimpleNoticeViewHolder(private val binding: ItemSimpleNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModel: DataModel) {
            val data = dataModel as DataModel.SimpleNotice
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(binding) {
                    cardHeader.text = data.name
                    description.text = data.description
                    //    root.setOnClickListener { listItemClicked(data) }
                }
            }
        }
    }

    inner class ScheduledEventViewHolder(private val binding: ItemScheduledEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModel: DataModel) {
            val data = dataModel as DataModel.ScheduledEvent
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(binding) {
                    tvHeader.text = "${data.date.toStdFormatString()}"
                    tvName.text = data.name
                    tvDescription.text = data.description

                    btnDelete.setOnClickListener {
                        notifyItemRemoved(layoutPosition)
                        listItemClicked(data)
                    }
                }
            }
        }
    }

}