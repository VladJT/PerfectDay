package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemNoteBinding
import jt.projects.utils.getAlertStringHowManyDaysBefore
import jt.projects.utils.toStdFormatString

class ScheduledEventViewHolder private constructor(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(
        dataModel: DataModel,
        listItemClicked: ((DataModel) -> Unit)?,
        onDeleteClicked: ((DataModel, Int) -> Unit)?
    ) {
        val data = dataModel as DataModel.ScheduledEvent
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvHeader.text = data.name
                tvDate.text = data.date.toStdFormatString()
                tvDescription.text = data.description

                tvDaysToEvent.isVisible = true
                tvDaysToEvent.text = getAlertStringHowManyDaysBefore(data.date)

                // удаление заметки
                btnDelete.setOnClickListener {
                    if (onDeleteClicked != null) {
                        onDeleteClicked(data, layoutPosition)
                    }
                }

                // редактирование заметки
                btnEdit.setOnClickListener {
                    if (listItemClicked != null) {
                        listItemClicked(data)
                    }
                }
            }
        }
    }
}