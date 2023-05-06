package jt.projects.repository.network.holiday

import jt.projects.repository.BuildConfig
import jt.projects.repository.network.holiday.dto.HolidayDTO
import jt.projects.repository.network.holiday.dto.calendarific.Holiday
import jt.projects.repository.network.holiday.dto.calendarific.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApi {
    @GET("/v1")
    suspend fun searchHoliday(
        @Query("country") country: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String,
        @Query("api_key") apiKey: String = BuildConfig.HOLIDAY_API_KEY,
    ): List<HolidayDTO>

    @GET("/api/v2/holidays")
    suspend fun searchHolidayCaledarific(
        @Query("country") country: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String,
        @Query("api_key") apiKey: String = BuildConfig.CALENDARIFIC_API_KEY,
    ): Holiday

}