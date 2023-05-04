package jt.projects.perfectday.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import jt.projects.utils.LOG_TAG
import jt.projects.utils.PUSH_NOTIFICATION_STARTHOUR
import jt.projects.utils.PUSH_NOTIFICATION_STARTMINUTE
import jt.projects.utils.PUSH_START
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PushSettingViewModel(private val sharedPref: SimpleSettingsPreferences) :
    ViewModel() {

    private val _hourTime = MutableStateFlow(0)
    val hourTime = _hourTime.asStateFlow()
    private val _minuteTime = MutableStateFlow(0)
    val minuteTime = _minuteTime.asStateFlow()

    private val _isOnPushService = MutableStateFlow(false)
    val isOnPushService get() = _isOnPushService.asStateFlow()

    init {
        checkPushServise()
    }

    private fun checkPushServise() {
        val onPushService = sharedPref.getBooleanOrFalse(PUSH_START)
        _isOnPushService.tryEmit(onPushService)

        if (onPushService) {
            initHourMinuteFromSharedPref()
        }
    }

    private fun initHourMinuteFromSharedPref() {
        var hourCurr = sharedPref.getIntOrZero(PUSH_NOTIFICATION_STARTHOUR)
        var minuteCurr = sharedPref.getIntOrZero(PUSH_NOTIFICATION_STARTMINUTE)

        _hourTime.tryEmit(hourCurr)
        _minuteTime.tryEmit(minuteCurr)
    }

    fun onClickButtonSwitchOnOffPush(isChecked: Boolean) {
        sharedPref.saveBoolean(PUSH_START, isChecked)
        checkPushServise()
    }

    fun onSelectHourData(hour: Int) {
        _hourTime.tryEmit(hour)
    }

    fun onSelectMinuteData(minute: Int) {
        Log.d(LOG_TAG, "onSelectMinuteData: $minute")
        _minuteTime.tryEmit(minute)
    }

    fun saveTimeToSharedPreferences() {
        sharedPref.saveInt(PUSH_NOTIFICATION_STARTHOUR, hourTime.value)
        sharedPref.saveInt(PUSH_NOTIFICATION_STARTMINUTE, minuteTime.value)
    }
}