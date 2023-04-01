package jt.projects.utils.shared_preferences

import android.content.SharedPreferences

class SimpleSharedPref(
    private val sharedPreferences: SharedPreferences
) {
    companion object{
        const val SP_DB_NAME = "settings"
    }

    fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun get(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}