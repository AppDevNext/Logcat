package info.hannes.crashlytic

import android.util.Log

import com.crashlytics.android.Crashlytics
import timber.log.Timber

@Suppress("unused")
class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority < Log.INFO) {
            return
        }

        CrashlyticsTrackerDelegate.setString("PRIORITY", when (priority) {
            2 -> "Verbose"
            3 -> "Debug"
            4 -> "Info"
            5 -> "Warn"
            6 -> "Error"
            7 -> "Assert"
            else -> priority.toString()
        })
        tag?.let { CrashlyticsTrackerDelegate.setString("TAG", it) }

        if (priority > Log.INFO) {
            Crashlytics.log(message)
        }
        if (throwable != null && priority > Log.INFO) {
            Crashlytics.logException(throwable)
        }
    }
}
