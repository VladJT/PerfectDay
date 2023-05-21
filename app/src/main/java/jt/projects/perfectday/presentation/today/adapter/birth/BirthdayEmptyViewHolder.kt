package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemEmptyBirthdayBinding

class BirthdayEmptyViewHolder private constructor(
    binding: ItemEmptyBirthdayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemEmptyBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    fun bind() {}
}