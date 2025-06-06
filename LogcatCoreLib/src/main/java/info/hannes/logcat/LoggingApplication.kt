package info.hannes.logcat

import android.app.Application
import info.hannes.timber.DebugFormatTree
import timber.log.Timber

open class LoggingApplication(private val delegator: Class<*>? = null) : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLogging()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected open fun setupLogging() {
        LoggingTools.globalErrorCatcher()
        Timber.plant(DebugFormatTree(delegator = delegator))
    }
}
