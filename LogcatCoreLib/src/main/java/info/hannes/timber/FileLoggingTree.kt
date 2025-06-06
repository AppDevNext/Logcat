package info.hannes.timber

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import info.hannes.logcat.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
@SuppressLint("LogNotTimber")
open class FileLoggingTree(externalCacheDir: File, context: Context? = null, filename: String = UUID.randomUUID().toString()) : DebugFormatTree() {

    var file: File
        private set

    val lastLogEntry: LiveData<Event<String>>
        get() = _lastLogEntry

    protected val _lastLogEntry = MutableLiveData<Event<String>>()

    private var logImpossible = false

    init {
        externalCacheDir.let {
            if (!it.exists()) {
                if (!it.mkdirs()) Log.e(LOG_TAG, "couldn't create ${it.absoluteFile}")
            }
            val fileNameTimeStamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            file = if (context != null) {
                File(it, "${context.packageName}.$fileNameTimeStamp.log")
            } else {
                File(it, "$filename.$fileNameTimeStamp.log")
            }
        }
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val logTimeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault()).format(Date())

            val priorityText = when (priority) {
                2 -> "V:"
                3 -> "D:"
                4 -> "I:"
                5 -> "W:"
                6 -> "E:"
                7 -> "A:"
                else -> "$priority"
            }

            val textLine = "$priorityText $logTimeStamp$tag$message\n"
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    val writer = FileWriter(file, true)
                    writer.append(textLine)
                    writer.flush()
                    writer.close()
                }
            }

            if (Thread.currentThread().name == "main") _lastLogEntry.value = Event(textLine)
            else Handler(Looper.getMainLooper()).post { _lastLogEntry.value = Event(textLine) }

        } catch (e: Exception) {
            // Log to prevent an endless loop
            if (!logImpossible) {
                // log this output just once
                Log.w(LOG_TAG, "Can't log into file : $e")
                logImpossible = true
            }
        }
        // Don't call super, otherwise it logs twice
        // super.log(priority, tag, message, t)
    }

    fun getFileName(): String = file.absolutePath

    companion object {

        private val LOG_TAG = FileLoggingTree::class.java.simpleName
    }
}
