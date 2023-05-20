package jt.projects.perfectday.presentation.today.adapter.birth

import androidx.recyclerview.widget.DiffUtil

class BirthdayDiffUtil : DiffUtil.ItemCallback<FriendItem>() {
    override fun areItemsTheSame(oldItem: FriendItem, newItem: FriendItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: FriendItem, newItem: FriendItem): Boolean =
        oldItem.hashCode() == newItem.hashCode()
}