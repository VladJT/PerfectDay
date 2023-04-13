package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemScheduledEventBinding
import jt.projects.utils.toStdFormatString

class NoteViewHolder private constructor(
    private val binding: ItemScheduledEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemScheduledEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.Notes, onClickDeleteNote: (Int) -> Unit) {
        val data = item.data


        with(binding) {
            tvHeader.text = "${data.name}"
            tvDate.text = "${data.date.toStdFormatString()}"
            tvDescription.text = data.description

            btnDelete.setOnClickListener {
                onClickDeleteNote.invoke(data.id)
            }
        }

    }
}