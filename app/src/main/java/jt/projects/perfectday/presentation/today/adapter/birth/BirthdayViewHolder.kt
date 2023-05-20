package jt.projects.perfectday.presentation.today.adapter.birth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.R
import jt.projects.perfectday.core.toStdFormatString
import jt.projects.perfectday.databinding.ItemBirthdayBinding
import jt.projects.utils.extensions.emptyString
import jt.projects.utils.extensions.loadWithPlaceHolder

private const val SPACE = " "

class BirthdayViewHolder private constructor(
    private val binding: ItemBirthdayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(friend: FriendItem) {
        with(binding) {
            val split = friend.name.split(SPACE)
            val firstName = split.getOrElse(0) { emptyString() }
            val lastName = split.getOrElse(1) { emptyString() }
            tvFullName.text = root.resources.getString(R.string.holiday_description, firstName, lastName)
            tvDateBirthday.text = friend.birthDate.toStdFormatString()
            tvAge.text = friend.age.toString()
            ivAvatarPhoto.loadWithPlaceHolder(friend.photoUrl)
        }
    }
}