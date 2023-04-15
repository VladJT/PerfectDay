package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ItemHolidayDayBinding
import jt.projects.utils.extensions.hideViewInRecycler
import jt.projects.utils.extensions.showViewInRecycler
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class HolidayViewHolder private constructor(
    private val binding: ItemHolidayDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup): this(
        ItemHolidayDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.Holiday) {
        if (item.holidays.isEmpty()) {
            itemView.hideViewInRecycler()
            return
        }
        itemView.showViewInRecycler()

        val holiday = item.holidays.first()
        setDate(holiday.date)
        setDescription(holiday)
    }

    private fun setDate(date: LocalDate) {
        binding.tvHolidayDate.text = binding.root.resources.getString(
            R.string.holiday_day,
            date.dayOfMonth,
            date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        )
    }

    private fun setDescription(holiday: DataModel.Holiday) {
        binding.tvHolidayDescription.text = binding.root.resources.getString(
            R.string.holiday_description,
            holiday.name,
            holiday.description
        )
    }
}