package myplayground.example.dicodingstory.util

import android.os.Build
import android.content.Intent
import android.os.Parcelable

class IntentParcel {
    companion object {
        inline fun <reified T : Parcelable?> parseIntent(intent: Intent, key: String): T? {
            return if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(key, T::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<T>(key)
            }
        }
    }
}