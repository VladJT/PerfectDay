package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.facts.FactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class SimpleNoticeInteractorImpl(
    private val repository: FactsRepository
) {

    suspend fun getFactsByDate(date: LocalDate, factsCount: Int): Flow<DataModel> {
        val result = mutableListOf<DataModel.SimpleNotice>()
        for (i in 1..factsCount) {
            result.add(repository.getFactByDate(date))
        }
        return flow { result.distinctBy {
            it.description
        }}
    }

    fun getFakeFacts(): List<DataModel.SimpleNotice> {
        return listOf(
            DataModel.SimpleNotice(
                "Новости технологий",
                "Правительству Российской Федерации совместно с автономной некоммерческой организацией «Агентство стратегических инициатив по продвижению новых проектов»"
            ),
            DataModel.SimpleNotice(
                "Fan ID",
                "Путин поручил упростить использование Fan ID для пенсионеров, детей и инвалидов"
            ),
            DataModel.SimpleNotice("Музыка", "Концерт «Ночных снайперов» отменили в Чебоксарах")
        )
    }

}