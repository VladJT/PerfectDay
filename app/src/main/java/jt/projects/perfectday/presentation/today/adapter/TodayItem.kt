package jt.projects.perfectday.presentation.today.adapter

import jt.projects.model.DataModel

sealed class TodayItem {
    data class Holiday(val holidays: List<DataModel.Holiday>) : TodayItem()
    data class Friends(
        val friendsVk: List<DataModel.BirthdayFromVk>? = null,
        val friendsPhone: List<DataModel.BirthdayFromPhone>? = null
    ): TodayItem()
    data class FactOfDay(val list: List<DataModel.SimpleNotice>) : TodayItem()
    data class Notes(val data: DataModel.ScheduledEvent) : TodayItem()
}
