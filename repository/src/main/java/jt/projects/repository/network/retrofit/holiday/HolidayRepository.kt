package jt.projects.repository.network.retrofit.holiday

import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import java.time.LocalDate

interface HolidayRepository {
    suspend fun getHoliday(country:String, date: LocalDate): List<HolidayDTO>
}