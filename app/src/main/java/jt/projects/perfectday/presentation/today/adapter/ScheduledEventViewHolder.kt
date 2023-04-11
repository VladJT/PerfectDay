package jt.projects.perfectday.presentation.today.adapter

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
        listItemClicked: (DataModel) -> Unit,
        onDeleteClicked: (Int) -> Unit
    ) {
        val data = dataModel as DataModel.ScheduledEvent
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvHeader.text = "${data.date.toStdFormatString()}"
                tvName.text = data.name
                tvDescription.text = data.description

                btnDelete.setOnClickListener {
                    onDeleteClicked(layoutPosition)
                    listItemClicked(data)
                }
            }
        }
    }
}