package jt.projects.repository.network.holiday.dto.calendarific

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("holidays")
    val holidays: List<HolidayCalendarificDto>
)
