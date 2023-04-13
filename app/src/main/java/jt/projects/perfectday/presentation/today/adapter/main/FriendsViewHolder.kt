package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.LayoutBithdayFriendsBinding
import jt.projects.perfectday.presentation.today.adapter.birth.BirthdayListAdapter
import jt.projects.utils.extensions.hideViewInRecycler
import jt.projects.utils.extensions.showViewInRecycler

class FriendsViewHolder private constructor(
    binding: LayoutBithdayFriendsBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val birthdayAdapter by lazy { BirthdayListAdapter() }

    init {
        binding.rvBirthday.adapter = birthdayAdapter
    }

    constructor(parent: ViewGroup): this(
        LayoutBithdayFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.Friends) {
        if (item.friendsVk.isEmpty()) {
            itemView.hideViewInRecycler()
            return
        }
        itemView.showViewInRecycler()
        birthdayAdapter.submitList(item.friendsVk)
    }
}