package jt.projects.perfectday.presentation.today.adapter.birth

import androidx.recyclerview.widget.DiffUtil
import jt.projects.model.DataModel

class BirthdayDiffUtil : DiffUtil.ItemCallback<DataModel.BirthdayFromVk>() {
    override fun areItemsTheSame(
        oldItem: DataModel.BirthdayFromVk,
        newItem: DataModel.BirthdayFromVk
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: DataModel.BirthdayFromVk,
        newItem: DataModel.BirthdayFromVk
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
}