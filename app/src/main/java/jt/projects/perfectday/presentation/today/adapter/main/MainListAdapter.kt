package jt.projects.perfectday.presentation.today.adapter.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel

private const val HOLIDAY_ITEM = 1
private const val FRIENDS_ITEM = 2
private const val FACT_OF_DAY_ITEM = 3
private const val NOTE_ITEM = 4

class MainListAdapter(
    private val onClickDeleteNote: (Int) -> Unit,
    private val onEditClickNote: (DataModel) -> Unit
): ListAdapter<TodayItem, RecyclerView.ViewHolder>(TodayDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            HOLIDAY_ITEM -> HolidayViewHolder(parent)
            FRIENDS_ITEM -> FriendsViewHolder(parent)
            FACT_OF_DAY_ITEM -> FactOfDayViewHolder(parent)
            NOTE_ITEM -> NoteViewHolder(parent)
            else -> throw IllegalStateException("Holder is missing for this element")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HolidayViewHolder -> holder.bind(getItem(position) as TodayItem.Holiday)
            is FriendsViewHolder -> holder.bind(getItem(position) as TodayItem.Friends)
            is FactOfDayViewHolder -> holder.bind(getItem(position) as TodayItem.FactOfDay)
            is NoteViewHolder ->
                holder.bind(
                    getItem(position) as TodayItem.Notes,
                    onClickDeleteNote,
                    onEditClickNote
                )
        }
    }

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)) {
            is TodayItem.Holiday -> HOLIDAY_ITEM
            is TodayItem.Friends -> FRIENDS_ITEM
            is TodayItem.FactOfDay -> FACT_OF_DAY_ITEM
            is TodayItem.Notes -> NOTE_ITEM
        }
}