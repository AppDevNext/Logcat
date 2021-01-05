package info.hannes.logcat

import android.app.Application
import info.hannes.timber.DebugTree
import timber.log.Timber

abstract class LoggingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLogging()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun setupLogging() {
        LoggingTools.globalErrorCatcher()
        Timber.plant(DebugTree())
    }

}
