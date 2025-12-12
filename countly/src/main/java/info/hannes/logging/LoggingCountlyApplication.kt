package info.hannes.logging

import android.app.Application
import info.hannes.countly.Analytics
import info.hannes.timber.CountlyTree
import info.hannes.timber.DebugFormatTree
import timber.log.Timber

abstract class LoggingCountlyApplication(
    private val countlyHost: String,
    private val countlyKey: String,
    private val debugBuildType: Boolean,
    private val serverIgnoreToken: String? = null
) : Application() {

    override fun onCreate() {
        super.onCreate()
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            @Suppress("ControlFlowWithEmptyBody")
            Timber.e(e.cause?.also { } ?: run { e })
            oldHandler?.uncaughtException(t, e)
        }

        setupLogging(!debugBuildType)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun setupLogging(countlyLogging: Boolean) {
        Timber.plant(DebugFormatTree())
        if (countlyLogging) {
            Analytics.initAnalytics(this, countlyLogging, countlyKey, countlyHost)
            Timber.plant(CountlyTree(Analytics(), serverIgnoreToken))
        }
    }

}
