package ir.amirrezaanari.sleeplock

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SleepPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("sleep_lock_prefs", Context.MODE_PRIVATE)

    var isSleepActive: Boolean
        get() = prefs.getBoolean(KEY_IS_SLEEP_ACTIVE, false)
        set(value) = prefs.edit { putBoolean(KEY_IS_SLEEP_ACTIVE, value) }

    var savedPin: String?
        get() = prefs.getString(KEY_SAVED_PIN, null)
        set(value) = prefs.edit { putString(KEY_SAVED_PIN, value) }

    fun clearSleepSession() {
        prefs.edit {
            putBoolean(KEY_IS_SLEEP_ACTIVE, false)
            remove(KEY_SAVED_PIN)
        }
    }

    companion object {
        private const val KEY_IS_SLEEP_ACTIVE = "is_sleep_active"
        private const val KEY_SAVED_PIN = "saved_pin"
    }
}
