package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.core.extensions.translateText
import jt.projects.perfectday.databinding.ItemFactOfDayBinding
import jt.projects.utils.extensions.hideViewInRecycler
import jt.projects.utils.extensions.showViewInRecycler

class FactOfDayViewHolder private constructor(
    private val binding: ItemFactOfDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemFactOfDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.FactOfDay) {
        if (item.list.isEmpty()) {
            itemView.hideViewInRecycler()
            return
        }
        itemView.showViewInRecycler()

        val text = item.list.first().description
        binding.tvFactDescription.text = text

        binding.tvFactDescription.translateText()
    }
}