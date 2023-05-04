package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.perfectday.core.translator.translateText
import jt.projects.repository.network.facts.FactsRepository
import java.time.LocalDate

class SimpleNoticeInteractorImpl(private val repository: FactsRepository) {

    suspend fun getFactsByDate(date: LocalDate, factsCount: Int): List<DataModel.SimpleNotice> {
        val result = mutableListOf<DataModel.SimpleNotice>()
        for (i in 1..factsCount) {
            result.add(repository.getFactByDate(date))
        }
        val distinctResult = result.distinctBy {
            it.description
        }

        distinctResult.forEach {
            it.description = it.description.translateText()
        }

        return distinctResult
    }
}