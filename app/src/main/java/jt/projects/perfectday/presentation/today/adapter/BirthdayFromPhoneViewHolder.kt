package jt.projects.perfectday.presentation.today.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ItemBirthdayFromPhoneBinding
import jt.projects.utils.ui.CoilImageLoader
import org.koin.java.KoinJavaComponent

class BirthdayFromPhoneViewHolder private constructor(
    private val binding: ItemBirthdayFromPhoneBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemBirthdayFromPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(dataModel: DataModel, listItemClicked: (DataModel) -> Unit) {
        val data = dataModel as DataModel.BirthdayFromPhone
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvName.text = data.name
                tvBirthday.text = data.birthDate.toString()
                tvAge.text = data.age.toString()

                KoinJavaComponent.getKoin().get<CoilImageLoader>()
                    .loadToCircleView(ivAvatar, R.drawable.default_avatar)
                root.setOnClickListener { listItemClicked(data) }
            }
        }
    }
}