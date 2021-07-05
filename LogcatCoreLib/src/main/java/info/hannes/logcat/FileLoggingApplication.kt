package info.hannes.logcat

import info.hannes.timber.FileLoggingTree
import timber.log.Timber

class FileLoggingApplication : LoggingApplication() {

    override fun setupLogging() {
        super.setupLogging()
        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }
    }
}
