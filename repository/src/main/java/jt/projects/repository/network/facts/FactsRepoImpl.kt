package jt.projects.repository.network.facts

import jt.projects.model.DataModel
import jt.projects.utils.*
import retrofit2.Retrofit
import java.time.LocalDate

class FactsRepoImpl(retrofit: Retrofit) : FactsRepository {
    private val api = retrofit.newBuilder()
        .baseUrl(FACTS_BASE_URL_LOCATIONS)
        .build()
        .create(FactsApi::class.java)

    override suspend fun getFactByDate(date: LocalDate): DataModel.SimpleNotice {
        val result = api.getFactOfTheDay(date.monthValue, date.dayOfMonth)
        return DataModel.SimpleNotice(FACT_HEADER, result.text)
    }
}