package jt.projects.perfectday.presentation.today.adapter.birth

import androidx.recyclerview.widget.DiffUtil
import jt.projects.model.DataModel

class BirthdayDiffUtil : DiffUtil.ItemCallback<DataModel.Friend>() {
    override fun areItemsTheSame(
        oldItem: DataModel.Friend,
        newItem: DataModel.Friend
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: DataModel.Friend,
        newItem: DataModel.Friend
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
}