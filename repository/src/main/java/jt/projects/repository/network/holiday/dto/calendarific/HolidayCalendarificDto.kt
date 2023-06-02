package jt.projects.repository.network.holiday.dto.calendarific

import com.google.gson.annotations.SerializedName

data class HolidayCalendarificDto(
    @SerializedName("canonical_url")
    val canonicalUrl: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("locations")
    val locations: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("primary_type")
    val type: String,

    @SerializedName("states")
    val states: String,

    @SerializedName("urlid")
    val urlid: String,
)