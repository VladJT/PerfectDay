package jt.projects.repository.network.holiday.dto

import com.google.gson.annotations.SerializedName

data class HolidayDTO(
    @SerializedName("country")
    val country: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("date_day")
    val dateDay: String,

    @SerializedName("date_month")
    val dateMonth: String,

    @SerializedName("date_year")
    val dateYear: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("language")
    val language: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("name_local")
    val nameLocal: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("week_day")
    val weekDay: String
)