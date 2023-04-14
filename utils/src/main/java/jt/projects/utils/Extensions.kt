package jt.projects.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/**
 * ACTIVITY EXTENSIONS
 */
fun Activity.showSnackbar(text: String) {
    Snackbar.make(
        this.findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Activity.showToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

/**
 * FRAGMENT EXTENSIONS
 */
fun Fragment.showSnackbar(text: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun LocalDate.toStdFormatString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}


/**
 * LOCAL_DATE EXTENSIONS
 */

fun getAlertStringHowManyDaysBefore(birthDate: LocalDate): String {
    val endDate = LocalDate.of(
        LocalDate.now().year,
        birthDate.month,
        birthDate.dayOfMonth
    )
    return when (val daysBeforeEventCount = ChronoUnit.DAYS.between(LocalDate.now(), endDate)) {
        0L -> "Сегодня"
        1L -> "Завтра"
        2L -> "Послезавтра"
        else -> "$daysBeforeEventCount дней до события"
    }
}


