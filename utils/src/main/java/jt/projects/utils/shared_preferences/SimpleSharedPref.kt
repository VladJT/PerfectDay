package jt.projects.utils.shared_preferences

import android.content.SharedPreferences
import jt.projects.utils.REMINDER_PERIOD_DEFAULT
import jt.projects.utils.REMINDER_PERIOD_KEY
import jt.projects.utils.extensions.emptyString

const val VK_AUTH_TOKEN = "vk_authorization_token"

class SimpleSharedPref(
    private val sharedPreferences: SharedPreferences
) : SimpleSettingsPreferences {
    companion object {
        const val SP_DB_NAME = "settings"
    }

    override fun saveSettings(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getSettings(key: String): String? =
        sharedPreferences.getString(key, emptyString())

    override fun getDaysPeriodForReminderFragment(): Long {
        val reminderPeriod = getSettings(REMINDER_PERIOD_KEY)
        return if (reminderPeriod.equals(emptyString())) REMINDER_PERIOD_DEFAULT
        else reminderPeriod!!.toLong()
    }
}