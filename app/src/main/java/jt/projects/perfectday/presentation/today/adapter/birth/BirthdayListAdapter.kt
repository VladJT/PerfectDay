package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import jt.projects.model.DataModel

class BirthdayListAdapter : ListAdapter<DataModel.Friend, BirthdayViewHolder>(BirthdayDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder =
        BirthdayViewHolder(parent)

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) =
        holder.bind(getItem(position))
}