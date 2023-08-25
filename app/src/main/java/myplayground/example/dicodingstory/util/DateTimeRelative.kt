package myplayground.example.dicodingstory.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateTimeRelative {
    companion object {
        fun parseTimeRelative(date: String): String {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("id", "ID"))
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")

            val timestampInMillis = isoFormat.parse(date)?.time
            val currentTimestampInMillis = System.currentTimeMillis()

            if (timestampInMillis == null) {
                return ""
            }

            val relativeTimeSpan = DateUtils.getRelativeTimeSpanString(
                timestampInMillis,
                currentTimestampInMillis,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )

            return relativeTimeSpan.toString()
        }
    }
}