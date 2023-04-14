package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemScheduledEventBinding
import jt.projects.utils.getAlertStringHowManyDaysBefore
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

            tvDaysToEvent.text = getAlertStringHowManyDaysBefore(data.date)
            tvDaysToEvent.isVisible = false

            btnDelete.setOnClickListener {
                onClickDeleteNote.invoke(data.id)
            }
        }

    }
}