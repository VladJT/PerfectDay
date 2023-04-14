package jt.projects.perfectday.core.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.databinding.ItemSimpleNoticeBinding
import jt.projects.perfectday.databinding.ItemSimpleNoticeHeaderOnlyBinding

class NoticeHeaderOnlyViewHolder private constructor(
    private val binding: ItemSimpleNoticeHeaderOnlyBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent:ViewGroup) :this(
        ItemSimpleNoticeHeaderOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(dataModel: DataModel) {
        val data = dataModel as DataModel.SimpleNotice
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                cardHeader.text = data.name
            }
        }
    }
}