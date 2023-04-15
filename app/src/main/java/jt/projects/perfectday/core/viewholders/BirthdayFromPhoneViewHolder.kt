package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemBirthdayFromPhoneBinding
import jt.projects.utils.getAlertStringHowManyDaysBefore
import jt.projects.utils.toStdFormatString
import jt.projects.utils.ui.CoilImageLoader
import org.koin.java.KoinJavaComponent

class BirthdayFromPhoneViewHolder private constructor(
    private val binding: ItemBirthdayFromPhoneBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemBirthdayFromPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(dataModel: DataModel, listItemClicked: ((DataModel) -> Unit)?) {
        val data = dataModel as DataModel.BirthdayFromPhone
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvName.text = data.name
                tvBirthday.text = data.birthDate.toStdFormatString()
                tvAge.text = data.age.toString()

                tvDaysToEvent.text = getAlertStringHowManyDaysBefore(data.birthDate)

                data.photoUri?.let {
                    KoinJavaComponent.getKoin().get<CoilImageLoader>()
                        .loadToCircleView(ivAvatar, it)
                }
                root.setOnClickListener {
                    if (listItemClicked != null) {
                        listItemClicked(data)
                    }
                }
            }
        }
    }
}