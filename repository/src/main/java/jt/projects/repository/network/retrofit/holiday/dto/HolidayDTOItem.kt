package jt.projects.repository.network.retrofit.holiday.dto

import com.google.gson.annotations.SerializedName

data class HolidayDTOItem(
    @field:SerializedName("country") val country: String,
    @field:SerializedName("date") val date: String,
    @field:SerializedName("date_day") val dateDay: String,
    @field:SerializedName("date_month") val dateMonth: String,
    @field:SerializedName("date_year") val dateYear: String,
    @field:SerializedName("description") val description: String,
    @field:SerializedName("language") val language: String,
    @field:SerializedName("location") val location: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("name_local") val nameLocal: String,
    @field:SerializedName("type") val type: String,
    @field:SerializedName("week_day") val weekDay: String
)