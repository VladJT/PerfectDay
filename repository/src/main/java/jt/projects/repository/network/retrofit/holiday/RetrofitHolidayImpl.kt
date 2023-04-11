package jt.projects.repository.network.retrofit.holiday

import jt.projects.repository.network.retrofit.BaseRetrofit
import jt.projects.repository.network.retrofit.DataSourceHoliday
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import jt.projects.utils.HOLIDAY_BASE_URL_LOCATION
import java.time.LocalDate

class RetrofitHolidayImpl(): DataSourceHoliday<List<HolidayDTO>> {

    private val retrofit: BaseRetrofit = BaseRetrofit.newInstansRetrofit
    override suspend fun getData(country:String, date: LocalDate): List<HolidayDTO> {

        return retrofit.getService(
                BaseInterceptor.interceptor, HOLIDAY_BASE_URL_LOCATION).searchHoliday(country,
                date.year.toString(),
                date.monthValue.toString(),
                date.dayOfMonth.toString())

    }



}