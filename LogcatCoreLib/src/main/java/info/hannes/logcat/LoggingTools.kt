package info.hannes.logcat

import timber.log.Timber

object LoggingTools {

    fun globalErrorCatcher() {
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            @Suppress("ControlFlowWithEmptyBody")
            Timber.e(e.cause?.also { } ?: run { e })
            oldHandler?.uncaughtException(t, e)
        }
    }
}
