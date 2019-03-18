package info.hannes.crashlytic

import android.util.Log

import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority < Log.INFO) {
            return
        }
        CrashlyticsTrackerDelegate.setInt("PRIORITY", priority)
        tag?.let { CrashlyticsTrackerDelegate.setString("TAG", it) }

        if (priority > Log.INFO) {
            Crashlytics.log(message)
        }
        if (throwable != null && priority > Log.INFO) {
            Crashlytics.logException(throwable)
        }
    }
}
