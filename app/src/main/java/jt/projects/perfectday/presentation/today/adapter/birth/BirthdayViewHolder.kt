package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ItemBirthdayBinding
import jt.projects.utils.extensions.loadWithPlaceHolder

class BirthdayViewHolder private constructor(
    private val binding: ItemBirthdayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(birthdayFriend: DataModel.Friend) {
        with(binding) {
            tvFullName.text = birthdayFriend.name
            tvAge.text = birthdayFriend.age.toString()
            ivAvatarPhoto.loadWithPlaceHolder(birthdayFriend.photoUrl)
        }
    }
}