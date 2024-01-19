package info.hannes.countly

import android.app.Activity

interface IAnalytics {

    fun isInitialized(): Boolean

    fun recordEvent(event: String)

    fun recordWarning(message: String)

    fun recordError(message: String)

    fun recordError(throwable: Throwable)

    fun onStart(activity: Activity?)

    fun onStop()
}
