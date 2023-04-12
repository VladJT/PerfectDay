package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemHolidayDayBinding

class HolidayViewHolder private constructor(
    private val binding: ItemHolidayDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemHolidayDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


}