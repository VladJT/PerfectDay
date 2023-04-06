package jt.projects.utils.shared_preferences

interface SimpleSettingsPreferences {
    fun saveSettings(key: String, value: String)

    fun getSettings(key: String): String?
}