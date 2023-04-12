package jt.projects.perfectday.presentation.calendar.dateFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ItemBirthdayFromPhoneBinding
import jt.projects.utils.ui.CoilImageLoader
import org.koin.java.KoinJavaComponent
import jt.projects.perfectday.core.MainAdapter

class ChosenDateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val BIRTHDAY_FROM_VK = 1
        const val BIRTHDAY_FROM_PHONE = 2
        const val UNKNOWN = -1
    }

    private var data: List<DataModel> = arrayListOf()

    fun setData(newData: List<DataModel>) {
        this.data = newData
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is DataModel.BirthdayFromPhone -> MainAdapter.BIRTHDAY_FROM_PHONE
            is DataModel.SimpleNotice -> MainAdapter.SIMPLE_NOTICE
            else -> MainAdapter.UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            MainAdapter.BIRTHDAY_FROM_PHONE -> {
                val binding = ItemBirthdayFromPhoneBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BirthdayFromPhoneViewHolder(binding)
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChosenDateAdapter.BirthdayFromPhoneViewHolder) {
            holder.bind(data[position])
        }
    }

    inner class BirthdayFromPhoneViewHolder(private val binding: ItemBirthdayFromPhoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModel: DataModel) {
            val data = dataModel as DataModel.BirthdayFromPhone
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(binding) {
                    tvName.text = data.name
                    tvBirthday.text = data.birthDate.toString()
                    tvAge.text = data.age.toString()
                    if (data.photoUri.isNullOrEmpty()) {
                        KoinJavaComponent.getKoin().get<CoilImageLoader>()
                            .loadToCircleView(ivAvatar, R.drawable.default_avatar)
                    } else {
                        KoinJavaComponent.getKoin().get<CoilImageLoader>()
                            .loadToCircleView(ivAvatar, data.photoUri.toString())
                    }
                }
            }
        }
    }
}