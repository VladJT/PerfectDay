package jt.projects.utils.shared_preferences

import android.content.SharedPreferences
import jt.projects.utils.extensions.emptyString

class SimpleSharedPref(
    private val sharedPreferences: SharedPreferences
): SimpleSettingsPreferences {
    companion object{
        const val SP_DB_NAME = "settings"
    }

    override fun saveSettings(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getSettings(key: String): String? =
        sharedPreferences.getString(key, emptyString())
}