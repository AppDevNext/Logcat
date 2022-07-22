package info.hannes.logcat

import info.hannes.timber.FileLoggingTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

open class FileLoggingApplication : LoggingApplication() {

    override fun setupLogging() {
        super.setupLogging()
        CoroutineScope(Dispatchers.IO).launch {
            externalCacheDir?.let {
                Timber.plant(FileLoggingTree(it, this@FileLoggingApplication))
            }
        }
    }
}
