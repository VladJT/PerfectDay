package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.repository.network.facts.FactsRepository
import java.time.LocalDate

class SimpleNoticeInteractorImpl(private val repository: FactsRepository) {
    suspend fun getFactsByDate(date: LocalDate, factsCount: Int): List<DataModel.SimpleNotice> {
        val result = mutableListOf<DataModel.SimpleNotice>()
        for (i in 1..factsCount) {
            result.add(repository.getFactByDate(date))
        }
        return result.distinctBy {
            it.description
        }
    }
}