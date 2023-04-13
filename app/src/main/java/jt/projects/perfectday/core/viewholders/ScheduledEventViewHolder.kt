package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemScheduledEventBinding
import jt.projects.utils.toStdFormatString

class ScheduledEventViewHolder private constructor(
    private val binding: ItemScheduledEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemScheduledEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(
        dataModel: DataModel,
        listItemClicked: ((DataModel) -> Unit)?,
        onDeleteClicked: ((DataModel, Int) -> Unit)?
    ) {
        val data = dataModel as DataModel.ScheduledEvent
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvHeader.text = "${data.name}"
                tvDate.text = "${data.date.toStdFormatString()}"
                tvDescription.text = data.description

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