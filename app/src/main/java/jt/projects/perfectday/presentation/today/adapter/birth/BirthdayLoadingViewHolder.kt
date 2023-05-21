package jt.projects.perfectday.presentation.today.adapter.birth

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.databinding.ItemBirthdayLoadingBinding

class BirthdayLoadingViewHolder private constructor(
    private val binding: ItemBirthdayLoadingBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemBirthdayLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind() {
        val animationDrawable = binding.root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(500)
        animationDrawable.setExitFadeDuration(1000)
        animationDrawable.start()
    }
}