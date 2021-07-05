package info.hannes.logcat

import info.hannes.timber.FileLoggingTree
import timber.log.Timber

open class FileLoggingApplication : LoggingApplication() {

    override fun setupLogging() {
        super.setupLogging()
        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }
    }
}
