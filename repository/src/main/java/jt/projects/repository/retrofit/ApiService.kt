package jt.projects.repository.retrofit

import jt.projects.repository.retrofit.holiday.dto.HolidayDTO
import jt.projects.repository.retrofit.holiday.dto.HolidayDTOItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v1")
    fun searchHoliday(@Query("api_key") apiKey:String, @Query("country") country:String, @Query("year") year: Int, @Query("month") month:Int, @Query("day") day:Int): Flow<HolidayDTO>
}