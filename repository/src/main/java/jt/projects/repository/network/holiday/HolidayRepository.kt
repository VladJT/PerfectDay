package jt.projects.repository.network.holiday

import jt.projects.repository.network.holiday.dto.HolidayDTO
import java.time.LocalDate

interface HolidayRepository {
    suspend fun getHoliday(country:String, date: LocalDate): List<HolidayDTO>
}