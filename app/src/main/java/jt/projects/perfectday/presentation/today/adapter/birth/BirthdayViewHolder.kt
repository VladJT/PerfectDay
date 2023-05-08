package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.core.toStdFormatString
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
            tvDateBirthday.text = birthdayFriend.birthDate.toStdFormatString()
            tvAge.text = birthdayFriend.age.toString()
            ivAvatarPhoto.loadWithPlaceHolder(birthdayFriend.photoUrl)
        }
    }
}