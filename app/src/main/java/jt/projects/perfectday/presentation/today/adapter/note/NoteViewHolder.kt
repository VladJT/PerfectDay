package jt.projects.perfectday.presentation.today.adapter.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemNoteBinding
import jt.projects.perfectday.core.toStdFormatString

class NoteViewHolder private constructor(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: NoteItem, onClickDeleteNote: (Int) -> Unit, onEditClickNote: (Int) -> Unit) {
        with(binding) {
            tvHeader.text = item.name
            tvDate.text = item.date.toStdFormatString()
            tvDescription.text = item.description

            btnDelete.setOnClickListener {
                onClickDeleteNote.invoke(item.id)
            }

            btnEdit.setOnClickListener {
                onEditClickNote.invoke(item.id)
            }
        }
    }
}