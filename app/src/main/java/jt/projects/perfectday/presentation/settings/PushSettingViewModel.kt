package jt.projects.perfectday.presentation.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import jt.projects.utils.LOG_TAG
import jt.projects.utils.PUSH_NOTIFICATION_STARTMINUTE
import jt.projects.utils.PUSH_NOTIFICATION_STARTHOUR
import jt.projects.utils.PUSH_START
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class PushSettingViewModel(private val sharedPref: SharedPreferences) : ViewModel() {

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

        val onPushService = sharedPref.getBoolean(PUSH_START,false)
        _isOnPushService.tryEmit(onPushService)

        val calendar = Calendar.getInstance()
        var hourCurr = calendar.get(Calendar.HOUR)
        var minuteCurr = calendar.get(Calendar.MINUTE)

        if(onPushService){
            hourCurr = sharedPref.getInt(PUSH_NOTIFICATION_STARTHOUR,0)
            minuteCurr = sharedPref.getInt(PUSH_NOTIFICATION_STARTMINUTE,0)
        }

        _hourTime.tryEmit(hourCurr)
        _minuteTime.tryEmit(minuteCurr)
    }

    fun onClickButtonSwitchOnOffPush(isChecked:Boolean){
        sharedPref.edit().putBoolean(PUSH_START,isChecked).apply()
       _isOnPushService.tryEmit(isChecked)
    }

    fun onSelectHourData(hour: Int) {
        sharedPref.edit().putInt(PUSH_NOTIFICATION_STARTHOUR,hour).apply()
        _hourTime.tryEmit(hour)
    }

    fun onSelectMinuteData(minute:Int){
        Log.d(LOG_TAG, "onSelectMinuteData: $minute")
       sharedPref.edit().putInt(PUSH_NOTIFICATION_STARTMINUTE, minute).apply()
        _minuteTime.tryEmit(minute)
    }
}