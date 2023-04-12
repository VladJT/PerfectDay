package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.LayoutBithdayFriendsBinding

class FriendsViewHolder private constructor(
    binding: LayoutBithdayFriendsBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        LayoutBithdayFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )



}