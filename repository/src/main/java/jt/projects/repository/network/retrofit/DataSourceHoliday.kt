package jt.projects.repository.network.retrofit

import java.time.LocalDate

interface DataSourceHoliday<T> {
    suspend fun getData(country:String, date:LocalDate):T
}