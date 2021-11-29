package info.hannes.logcat

import android.app.Application
import info.hannes.timber.DebugTree
import timber.log.Timber

open class LoggingDelegatorApplication(private val delegator: Class<*>) : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLogging()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected open fun setupLogging() {
        LoggingTools.globalErrorCatcher()
        Timber.plant(DebugTree(delegator = delegator))
    }
}
