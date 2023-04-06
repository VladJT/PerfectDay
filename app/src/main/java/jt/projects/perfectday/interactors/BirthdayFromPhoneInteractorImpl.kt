package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import java.time.LocalDate

class BirthdayFromPhoneInteractorImpl() {

    fun getDataByDate(date: LocalDate): List<DataModel.BirthdayFromPhone> {
        return listOf(
            DataModel.BirthdayFromPhone("Ivan", LocalDate.now(), 30, "url"),
            DataModel.BirthdayFromPhone("Semen", LocalDate.now(), 23, "url2"),
        )
    }

}