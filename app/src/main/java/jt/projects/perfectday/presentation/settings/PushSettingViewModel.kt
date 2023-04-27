package jt.projects.perfectday.presentation.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import jt.projects.utils.LOG_TAG
import jt.projects.utils.PUSH_NOTIFICATION_STARTMINUTE
import jt.projects.utils.PUSH_NOTIFICATION_STARTHOUR
import jt.projects.utils.PUSH_START
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class PushSettingViewModel(private val sharedPref: SimpleSettingsPreferences) : ViewModel() {

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

        val onPushService = sharedPref.getSettings(PUSH_START)?.toBooleanStrictOrNull()?:false
        _isOnPushService.tryEmit(onPushService)

        val calendar = Calendar.getInstance()
        var hourCurr = calendar.get(Calendar.HOUR)
        var minuteCurr = calendar.get(Calendar.MINUTE)

        if(onPushService){
            hourCurr = sharedPref.getSettings(PUSH_NOTIFICATION_STARTHOUR)?.toIntOrNull()?:0
            minuteCurr = sharedPref.getSettings(PUSH_NOTIFICATION_STARTMINUTE)?.toIntOrNull()?:0
        }
        sharedPref.saveSettings(PUSH_NOTIFICATION_STARTHOUR,hourCurr.toString())
        sharedPref.saveSettings(PUSH_NOTIFICATION_STARTMINUTE, minuteCurr.toString())

        _hourTime.tryEmit(hourCurr)
        _minuteTime.tryEmit(minuteCurr)
    }

    fun onClickButtonSwitchOnOffPush(isChecked:Boolean){
        sharedPref.saveSettings(PUSH_START,isChecked.toString())
       _isOnPushService.tryEmit(isChecked)
    }

    fun onSelectHourData(hour: Int) {
        sharedPref.saveSettings(PUSH_NOTIFICATION_STARTHOUR,hour.toString())
        _hourTime.tryEmit(hour)
    }

    fun onSelectMinuteData(minute:Int){
        Log.d(LOG_TAG, "onSelectMinuteData: $minute")
       sharedPref.saveSettings(PUSH_NOTIFICATION_STARTMINUTE, minute.toString())
        _minuteTime.tryEmit(minute)
    }
}