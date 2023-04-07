package jt.projects.repository.retrofit.facts

import jt.projects.model.DataModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FactsRepository {
    suspend fun getFactByDate(date: LocalDate): DataModel
}