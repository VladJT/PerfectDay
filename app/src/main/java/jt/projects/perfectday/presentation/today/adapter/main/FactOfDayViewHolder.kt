package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemFactOfDayBinding

class FactOfDayViewHolder private constructor(
    private val binding: ItemFactOfDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemFactOfDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.FactOfDay) {
        binding.root.isVisible = item.list.isNotEmpty()
        binding.tvFactDescription.text = item.list.first().description
    }
}