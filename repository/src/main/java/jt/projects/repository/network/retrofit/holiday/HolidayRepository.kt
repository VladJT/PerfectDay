package jt.projects.repository.network.retrofit.holiday

import android.icu.util.LocaleData
import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HolidayRepository {
    suspend fun getHoliday(apiKey:String, country:String, date: LocalDate): List<HolidayDTO>
}