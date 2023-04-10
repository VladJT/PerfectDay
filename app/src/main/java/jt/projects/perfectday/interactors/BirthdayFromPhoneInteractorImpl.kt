package jt.projects.perfectday.interactors


import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import jt.projects.model.DataModel
import java.time.LocalDate

class BirthdayFromPhoneInteractorImpl(applicationContext: Context) {

    private val context = applicationContext

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

    private fun makeBirthdayList(cursorWithContacts: Cursor?): List<DataModel.BirthdayFromPhone> {
        val returnList = mutableListOf<DataModel.BirthdayFromPhone>()
        while (cursorWithContacts!!.moveToNext()) {
            val id: String =
                cursorWithContacts.getString(cursorWithContacts.getColumnIndex(ContactsContract.Contacts._ID))
            val name =
                cursorWithContacts.getString(cursorWithContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val bd: ContentResolver = context.contentResolver
            val bdc = bd.query(
                ContactsContract.Data.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Event.DATA),
                ContactsContract.Data.CONTACT_ID + " = " + id + " AND " + ContactsContract.Contacts.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                null,
                ContactsContract.Data.DISPLAY_NAME
            )
            var localDate = LocalDate.now()
            if (bdc!!.count > 0) {
                while (bdc.moveToNext()) {
                    val year = (bdc.getString(0)[0].toString() + bdc.getString(0)[1]
                        .toString() + bdc.getString(0)[2].toString() + bdc.getString(0)[3].toString()).toInt()
                    val month = (bdc.getString(0)[5].toString() + bdc.getString(0)[6]
                        .toString()).toInt()
                    val day = (bdc.getString(0)[8].toString() + bdc.getString(0)[9]
                        .toString()).toInt()
                    localDate = LocalDate.of(year, month, day)
                }
            }
            bdc.close()
            returnList.add(DataModel.BirthdayFromPhone(name, localDate, null, null))
        }
        cursorWithContacts.close()
        return returnList
    }
}