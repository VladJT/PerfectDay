package jt.projects.perfectday.presentation.today.adapter.note

import androidx.recyclerview.widget.DiffUtil

class NoteDiffUtil: DiffUtil.ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}