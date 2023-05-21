package jt.projects.perfectday.presentation.today.adapter.note

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class NoteListAdapter(
    private val onClickDeleteNote: (Int) -> Unit,
    private val onEditClickNote: (Int) -> Unit
): ListAdapter<NoteItem, NoteViewHolder>(NoteDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(parent)

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), onClickDeleteNote, onEditClickNote)
    }
}