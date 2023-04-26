package jt.projects.utils.shared_preferences

interface SimpleSettingsPreferences {
    fun saveString(key: String, value: String)
    fun saveBoolean(key: String, value: Boolean)
    fun saveInt(key: String, value: Int)

    fun getStringOrEmptyString(key: String): String?
    fun getIntOrZero(key: String): Int
    fun getBooleanOrFalse(key: String): Boolean
    fun getBooleanOrTrue(key: String): Boolean
    fun getDaysPeriodForReminderFragment(): Long
}