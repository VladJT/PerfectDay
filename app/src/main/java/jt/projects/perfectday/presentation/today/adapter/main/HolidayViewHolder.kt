package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemHolidayDayBinding
import jt.projects.utils.extensions.hideViewInRecycler

class HolidayViewHolder private constructor(
    private val binding: ItemHolidayDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemHolidayDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.Holiday) {
        if (item.holidays.isEmpty()) {
            itemView.hideViewInRecycler()
            return
        }
        itemView.isVisible = true
    }

}