package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemBirthdayFromVkBinding
import jt.projects.utils.toStdFormatString
import jt.projects.utils.ui.CoilImageLoader
import org.koin.java.KoinJavaComponent

class BirthdayFromVKViewHolder private constructor(
    private val binding: ItemBirthdayFromVkBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemBirthdayFromVkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(dataModel: DataModel, listItemClicked: ((DataModel) -> Unit)?) {
        val data = dataModel as DataModel.BirthdayFromVk
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvName.text = data.name
                tvBirthday.text = data.birthDate.toStdFormatString()
                tvAge.text = data.age.toString()

                data.photoUrl?.let {
                    KoinJavaComponent.getKoin().get<CoilImageLoader>()
                        .loadToCircleView(ivAvatar, it)
                }
                root.setOnClickListener {
                    if (listItemClicked != null) {
                        listItemClicked(data)
                    }
                }
            }
        }
    }
}