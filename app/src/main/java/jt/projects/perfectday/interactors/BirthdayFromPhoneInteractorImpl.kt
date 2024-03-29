package jt.projects.perfectday.interactors


import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import jt.projects.model.DataModel
import jt.projects.perfectday.core.getAge
import jt.projects.perfectday.core.sortListByDateDifferentYear
import java.time.LocalDate

class BirthdayFromPhoneInteractorImpl(applicationContext: Context) {

    private val context = applicationContext

    fun getFakeDataByDate(date: LocalDate): List<DataModel.BirthdayFromPhone> {
        return listOf(
            DataModel.BirthdayFromPhone("1", "Ivan", LocalDate.now(), 30, "url"),
            DataModel.BirthdayFromPhone("2", "Semen", LocalDate.now(), 23, "url2"),
        )
    }

    suspend fun getContactsByDay(localDate: LocalDate): List<DataModel.BirthdayFromPhone> {
        return makeBirthdayListByDay(getContacts(), localDate)
    }

    suspend fun getContactsInInterval(
        startIntervalDate: LocalDate,
        endIntervalDate: LocalDate
    ): List<DataModel.BirthdayFromPhone> {
        return makeBirthdayListInInterval(getContacts(), startIntervalDate, endIntervalDate)
    }

    suspend fun getFriendsByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.BirthdayFromPhone> = getContactsInInterval(startDate, endDate)

    suspend fun getContacts(): List<DataModel.BirthdayFromPhone> {
        val contentResolver: ContentResolver = context.contentResolver
        val cursorWithContacts: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        return makeBirthdayList(cursorWithContacts)
    }

    private fun makeBirthdayListInInterval(
        prefilteredList: List<DataModel.BirthdayFromPhone>,
        startIntervalDate: LocalDate,
        endIntervalDate: LocalDate
    ): List<DataModel.BirthdayFromPhone> {
        val returnList = mutableListOf<DataModel.BirthdayFromPhone>()
        prefilteredList.forEach {
            if (startIntervalDate.year == endIntervalDate.year) {
                if (
                    startIntervalDate <= LocalDate.of(
                        startIntervalDate.year,
                        it.birthDate.monthValue,
                        it.birthDate.dayOfMonth
                    ) &&
                    endIntervalDate >= LocalDate.of(
                        startIntervalDate.year,
                        it.birthDate.monthValue,
                        it.birthDate.dayOfMonth
                    )
                ) {
                    returnList.add(it)
                }
            } else {
                if (
                    startIntervalDate <= LocalDate.of(
                        startIntervalDate.year,
                        it.birthDate.monthValue,
                        it.birthDate.dayOfMonth
                    ) ||
                    endIntervalDate >= LocalDate.of(
                        endIntervalDate.year,
                        it.birthDate.monthValue,
                        it.birthDate.dayOfMonth
                    )
                ) {
                    returnList.add(it)
                }

            }
        }
        if (startIntervalDate.year == endIntervalDate.year) {
            return sortListByDate(returnList)
        } else {
            return sortListByDateDifferentYear(returnList, startIntervalDate)
        }
    }

    private fun sortListByDate(
        returnList: MutableList<DataModel.BirthdayFromPhone>
    ): List<DataModel.BirthdayFromPhone> {
        return returnList.toList().sortedBy {
            LocalDate.of(LocalDate.now().year, it.birthDate.monthValue, it.birthDate.dayOfMonth)
        }
    }

    private fun makeBirthdayListByDay(
        prefilteredList: List<DataModel.BirthdayFromPhone>,
        localDate: LocalDate
    ): List<DataModel.BirthdayFromPhone> {
        val returnList = mutableListOf<DataModel.BirthdayFromPhone>()
        prefilteredList.forEach {
            if (
                it.birthDate.monthValue == localDate.monthValue &&
                it.birthDate.dayOfMonth == localDate.dayOfMonth
            ) {
                returnList.add(it)
            }
        }
        return returnList
    }

    private fun makeBirthdayList(cursorWithContacts: Cursor?): List<DataModel.BirthdayFromPhone> {
        val returnList = mutableListOf<DataModel.BirthdayFromPhone>()
        while (cursorWithContacts!!.moveToNext()) {
            val id: String =
                cursorWithContacts.getString(cursorWithContacts.getColumnIndex(ContactsContract.Contacts._ID))
            val name =
                cursorWithContacts.getString(cursorWithContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val photoUri =
                cursorWithContacts.getString(cursorWithContacts.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
            val birthDate: ContentResolver = context.contentResolver
            val birthDateCursor = birthDate.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Event.DATA),
                ContactsContract.Data.CONTACT_ID + " = " + id + " AND " + ContactsContract.Contacts.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                null,
                ContactsContract.Data.DISPLAY_NAME
            )
            var localDate: LocalDate
            if (birthDateCursor!!.count > 0) {
                while (birthDateCursor.moveToNext()) {
                    if (!birthDateCursor.getString(0).isNullOrEmpty()) {
                        val split = birthDateCursor.getString(0).split("-")
                        if (split.size != 3) continue

                        val year =
                            (birthDateCursor.getString(0)[0].toString() + birthDateCursor.getString(
                                0
                            )[1]
                                .toString() + birthDateCursor.getString(0)[2].toString() + birthDateCursor.getString(
                                0
                            )[3].toString()).toInt()
                        val month =
                            (birthDateCursor.getString(0)[5].toString() + birthDateCursor.getString(
                                0
                            )[6]
                                .toString()).toInt()
                        val day =
                            (birthDateCursor.getString(0)[8].toString() + birthDateCursor.getString(
                                0
                            )[9]
                                .toString()).toInt()
                        localDate = LocalDate.of(year, month, day)
                        val age = getAge(localDate)
                        returnList.add(
                            DataModel.BirthdayFromPhone(
                                id,
                                name,
                                localDate,
                                age,
                                photoUri
                            )
                        )
                    }
                }
            }
            birthDateCursor.close()
        }
        cursorWithContacts.close()
        return returnList
    }

}