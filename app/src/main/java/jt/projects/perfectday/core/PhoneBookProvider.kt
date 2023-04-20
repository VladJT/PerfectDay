package jt.projects.perfectday.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import jt.projects.model.DataModel
import jt.projects.utils.LOG_TAG

class PhoneBookProvider(val context: Context) {

    // открываем карточку контакта в телефонной книге
    fun openContact(data: DataModel.BirthdayFromPhone) {
        try {
            val contactId = data.id //  ID контакта
            val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
            val intent = Intent(Intent.ACTION_VIEW, contactUri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message.toString())
        }
    }
}