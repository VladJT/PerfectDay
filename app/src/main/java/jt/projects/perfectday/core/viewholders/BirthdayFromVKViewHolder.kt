package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemGeneralBirthdayBinding
import jt.projects.utils.extensions.loadWithPlaceHolder
import jt.projects.perfectday.core.getAlertStringHowManyDaysBefore
import jt.projects.perfectday.core.toStdFormatString

class BirthdayFromVKViewHolder private constructor(
    private val binding: ItemGeneralBirthdayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemGeneralBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(dataModel: DataModel, onItemClicked: ((DataModel) -> Unit)?) {
        val data = dataModel as DataModel.BirthdayFromVk
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvName.text = data.name
                tvBirthday.text = data.birthDate.toStdFormatString()
                tvAge.text = data.age.toString()

                tvDaysToEvent.text = getAlertStringHowManyDaysBefore(data.birthDate)
                ivAvatar.loadWithPlaceHolder(data.photoUrl)

                btnCallContact.isVisible = false

                root.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }
    }
}