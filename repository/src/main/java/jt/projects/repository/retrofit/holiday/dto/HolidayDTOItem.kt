package jt.projects.repository.retrofit.holiday.dto

import com.google.gson.annotations.SerializedName

data class HolidayDTOItem(
    @field:SerializedName("country") val country: String?,
    @field:SerializedName("date") val date: String?,
    @field:SerializedName("date_day") val date_day: String?,
    @field:SerializedName("date_month") val date_month: String?,
    @field:SerializedName("date_year") val date_year: String?,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("language") val language: String?,
    @field:SerializedName("location") val location: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("name_local") val name_local: String?,
    @field:SerializedName("type") val type: String?,
    @field:SerializedName("week_day") val week_day: String?
)