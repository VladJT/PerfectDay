package jt.projects.perfectday.core

import java.time.LocalDate

class DateStrategy(
    private val startDate: (() -> LocalDate)? = null,
    private val endDate: (() -> LocalDate)? = null
) {
    fun getStartDate(): LocalDate = startDate!!.invoke()
    fun getEndDate(): LocalDate = endDate!!.invoke()

    fun hasDates(): Boolean = (startDate?.invoke() != null && endDate?.invoke() != null)
}