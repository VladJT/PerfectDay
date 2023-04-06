package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import java.time.LocalDate

class SimpleNoticeInteractorImpl() {

    fun getData(): List<DataModel.SimpleNotice> {
        return listOf(
            DataModel.SimpleNotice("Новости технологий", "Правительству Российской Федерации совместно с автономной некоммерческой организацией «Агентство стратегических инициатив по продвижению новых проектов»"),
            DataModel.SimpleNotice("Fan ID", "Путин поручил упростить использование Fan ID для пенсионеров, детей и инвалидов"),
            DataModel.SimpleNotice("Музыка", "Концерт «Ночных снайперов» отменили в Чебоксарах")

        )
    }

}