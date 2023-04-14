package jt.projects.perfectday.presentation.today.adapter.main

import androidx.recyclerview.widget.DiffUtil

class TodayDiffUtil : DiffUtil.ItemCallback<TodayItem>() {
    override fun areItemsTheSame(oldItem: TodayItem, newItem: TodayItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TodayItem, newItem: TodayItem): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}