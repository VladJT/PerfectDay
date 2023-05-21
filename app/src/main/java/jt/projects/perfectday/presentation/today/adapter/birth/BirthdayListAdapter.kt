package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

private const val LOADING_FRIEND = 0
private const val EMPTY_FRIEND = 1
private const val FRIEND = 2

class BirthdayListAdapter : ListAdapter<FriendItem, RecyclerView.ViewHolder>(BirthdayDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            LOADING_FRIEND -> BirthdayLoadingViewHolder(parent)
            EMPTY_FRIEND -> BirthdayEmptyViewHolder(parent)
            FRIEND -> BirthdayViewHolder(parent)
            else -> throw IllegalStateException("Unknown view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when(holder) {
            is BirthdayLoadingViewHolder -> holder.bind()
            is BirthdayEmptyViewHolder -> holder.bind()
            is BirthdayViewHolder -> holder.bind(getItem(position))
            else -> throw IllegalStateException("Unknown view holder")
        }

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)) {
            FriendItem.LOADING -> LOADING_FRIEND
            FriendItem.EMPTY -> EMPTY_FRIEND
            else -> FRIEND
        }
}