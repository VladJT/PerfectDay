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

    fun getAllData(): List<DataModel.BirthdayFromPhone> {
        return listOf(
            DataModel.BirthdayFromPhone("Ivan", LocalDate.now(), 30, "url"),
            DataModel.BirthdayFromPhone("Semen", LocalDate.now(), 23, "url2"),
            DataModel.BirthdayFromPhone("Vasya", LocalDate.of(1990, 5, 23), 33, "url3"),
            DataModel.BirthdayFromPhone("Petya", LocalDate.of(1992, 4, 26), 31, "url4"),
            DataModel.BirthdayFromPhone("Volodya", LocalDate.of(1995, 4, 26), 28, "url5")
        )
    }

}