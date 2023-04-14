package jt.projects.perfectday.presentation.today.adapter.main

import jt.projects.model.DataModel

sealed class TodayItem {
    data class Holiday(val holidays: List<DataModel.Holiday>) : TodayItem()
    data class Friends(val friendsVk: List<DataModel.BirthdayFromVk>): TodayItem()
    data class FactOfDay(val list: List<DataModel.SimpleNotice>) : TodayItem()
    data class Notes(val data: DataModel.ScheduledEvent) : TodayItem()
}
