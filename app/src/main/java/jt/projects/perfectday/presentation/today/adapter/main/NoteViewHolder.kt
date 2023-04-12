package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemScheduledEventBinding

class NoteViewHolder private constructor(
    private val binding: ItemScheduledEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemScheduledEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}