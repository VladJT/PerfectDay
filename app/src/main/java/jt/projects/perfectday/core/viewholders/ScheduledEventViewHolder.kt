package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemNoteBinding
import jt.projects.perfectday.core.getAlertStringHowManyDaysBeforeScheduledEvent
import jt.projects.perfectday.core.toStdFormatString

class ScheduledEventViewHolder private constructor(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(
        dataModel: DataModel,
        onEditNoteClicked: ((Int) -> Unit)?,
        onDeleteClicked: ((Int) -> Unit)?
    ) {
        val data = dataModel as DataModel.ScheduledEvent
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvHeader.text = data.name
                tvDate.text = data.date.toStdFormatString()
                tvDescription.text = data.description

                tvDaysToEvent.isVisible = true
                tvDaysToEvent.text = getAlertStringHowManyDaysBeforeScheduledEvent(data.date)

                // удаление заметки
                btnDelete.setOnClickListener {
                    onDeleteClicked?.invoke(data.id)
                }

                // редактирование заметки
                btnEdit.setOnClickListener {
                    onEditNoteClicked?.invoke(data.id)
                }
            }
        }
    }
}