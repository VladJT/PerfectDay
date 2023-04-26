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

    /**
     * SAVE
     */
    override fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }


    /**
     * LOAD
     */
    override fun getStringOrEmptyString(key: String): String? =
        sharedPreferences.getString(key, emptyString())

    override fun getIntOrZero(key: String): Int = sharedPreferences.getInt(key, 0)

    override fun getBooleanOrFalse(key: String) = sharedPreferences.getBoolean(key, false)

    override fun getBooleanOrTrue(key: String) = sharedPreferences.getBoolean(key, true)

    override fun getDaysPeriodForReminderFragment(): Long {
        val reminderPeriod = getIntOrZero(REMINDER_PERIOD_KEY)
        return if (reminderPeriod == 0) REMINDER_PERIOD_DEFAULT
        else reminderPeriod.toLong()
    }

}