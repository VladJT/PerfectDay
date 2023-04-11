package jt.projects.repository.network.retrofit

import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApi {
    @GET("/v1")
   suspend fun searchHoliday(@Query("api_key") apiKey:String, @Query("country") country:String, @Query("year") year: String, @Query("month") month:String, @Query("day") day:String): List<HolidayDTO>
}