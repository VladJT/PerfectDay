package jt.projects.repository.retrofit.facts

import jt.projects.model.DataModel
import jt.projects.utils.FACTS_BASE_URL_LOCATIONS
import jt.projects.utils.FACT_HEADER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalDate

class FactsRepoImpl : FactsRepository {
    override suspend fun getFactByDate(date: LocalDate): DataModel.SimpleNotice {
        val response =
            withContext(CoroutineScope(Dispatchers.IO + SupervisorJob()).coroutineContext) {
                URL(FACTS_BASE_URL_LOCATIONS.plus("${date.monthValue}/${date.dayOfMonth}/date")).readText()
            }

        return DataModel.SimpleNotice(FACT_HEADER, response)
    }

}