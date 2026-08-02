package com.example.service

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object RefreshScheduler {

    val REFRESH_HOURS = listOf(8, 12, 18) // 8:00 AM, 12:00 PM, 6:00 PM (18:00)

    data class ScheduleStatus(
        val currentSessionLabel: String,
        val nextRefreshTimeLabel: String,
        val minutesRemainingToNext: Long,
        val lastRefreshedTimeStr: String
    )

    fun getScheduleStatus(lastRefreshedMillis: Long): ScheduleStatus {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)

        val currentSessionLabel = when {
            currentHour < 8 -> "8:00 AM Digest (Early Morning)"
            currentHour in 8..11 -> "8:00 AM Morning Dispatch"
            currentHour in 12..17 -> "12:00 PM Midday Dispatch"
            else -> "6:00 PM Evening Digest"
        }

        val nextTargetCal = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (currentHour < 8) {
            nextTargetCal.set(Calendar.HOUR_OF_DAY, 8)
            nextTargetCal.set(Calendar.MINUTE, 0)
        } else if (currentHour in 8..11) {
            nextTargetCal.set(Calendar.HOUR_OF_DAY, 12)
            nextTargetCal.set(Calendar.MINUTE, 0)
        } else if (currentHour in 12..17) {
            nextTargetCal.set(Calendar.HOUR_OF_DAY, 18)
            nextTargetCal.set(Calendar.MINUTE, 0)
        } else {
            nextTargetCal.add(Calendar.DAY_OF_YEAR, 1)
            nextTargetCal.set(Calendar.HOUR_OF_DAY, 8)
            nextTargetCal.set(Calendar.MINUTE, 0)
        }

        val diffMs = nextTargetCal.timeInMillis - now.timeInMillis
        val diffMins = (diffMs / (1000 * 60)).coerceAtLeast(1)

        val targetFormat = SimpleDateFormat("h:mm a", Locale.US)
        val isTomorrow = nextTargetCal.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)
        val dayPrefix = if (isTomorrow) "Tomorrow" else "Today"
        val nextRefreshLabel = "$dayPrefix ${targetFormat.format(nextTargetCal.time)}"

        val timeFormat = SimpleDateFormat("h:mm:ss a", Locale.US)
        val lastRefreshedLabel = if (lastRefreshedMillis > 0) {
            timeFormat.format(Date(lastRefreshedMillis))
        } else {
            "Just now"
        }

        return ScheduleStatus(
            currentSessionLabel = currentSessionLabel,
            nextRefreshTimeLabel = nextRefreshLabel,
            minutesRemainingToNext = diffMins,
            lastRefreshedTimeStr = lastRefreshedLabel
        )
    }

    fun isRefreshDue(lastRefreshedMillis: Long): Boolean {
        if (lastRefreshedMillis <= 0) return true
        val lastCal = Calendar.getInstance().apply { timeInMillis = lastRefreshedMillis }
        val nowCal = Calendar.getInstance()

        if (nowCal.get(Calendar.DAY_OF_YEAR) != lastCal.get(Calendar.DAY_OF_YEAR)) {
            return true
        }

        val lastHour = lastCal.get(Calendar.HOUR_OF_DAY)
        val nowHour = nowCal.get(Calendar.HOUR_OF_DAY)

        for (targetHour in REFRESH_HOURS) {
            if (lastHour < targetHour && nowHour >= targetHour) {
                return true
            }
        }
        return false
    }
}
