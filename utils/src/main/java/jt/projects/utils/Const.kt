package jt.projects.utils

import java.time.LocalDate

/**
 * URLS
 */
const val FACTS_BASE_URL_LOCATIONS = "http://numbersapi.com/"
const val HOLIDAY_BASE_URL_LOCATION = "https://holidays.abstractapi.com/"
const val HOLIDAY_COUNTRY = "Ru"


/**
 * SHARED PREFERENCES KEYS
 */
const val REMINDER_PERIOD_KEY =
    "REMINDER_PERIOD_KEY" // ключ shared_pref - количество дней для отображения напоминаний на вкладке REMINDER
const val IS_FIRST_TIME_START_APP_KEY =
    "isFirstTimeStartAppKey" // признак первого запуска приложения

/**
 * COMMON SETTINGS
 */
const val FACTS_COUNT = 1 // кол-во интересных фактов для отображения на вкладке "Today"
const val FACT_HEADER = "Интересный факт" // заголовок для полученного факта (name)
const val REMINDER_PERIOD_DEFAULT =
    7L // количество дней для отображения напоминаний на вкладке REMINDER

/**
 * EXCEPTIONS
 */
val ViewModelNotInitException = IllegalStateException("The ViewModel should be initialised first")
val InteractorException = IllegalStateException("Something wrong in interactor")


/**
 * LOGS
 */
const val LOG_TAG = "TAG"


/**
 * DI
 */
const val NAME_REMOTE = "Remote"
const val NAME_LOCAL = "Local"
const val NETWORK_SERVICE = "NETWORK_SERVICE"


/**
 * BUILD CONFIG
 */
const val FAKE = "FAKE"
const val REAL = "REAL"

/**
 * CONTACTS PERMISSION REQUEST
 */
const val REQUEST_CODE_READ_CONTACTS = 4513
var permissionGranted = false

/*
* FOR DEBUG LO
 */
val DEBUG: Boolean = BuildConfig.DEBUG && true

/*
* CHOSEN DATE VARIABLE
 */
var chosenCalendarDate: LocalDate = LocalDate.now()
