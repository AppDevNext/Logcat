package info.hannes.timber

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
class FileLoggingTree(externalCacheDir: File, context: Context) : Timber.DebugTree() {

    lateinit var file: File
        private set

    init {
        val logFiles = externalCacheDir.list { directory, filename ->
            directory.length() > 0 && filename.endsWith(".log") && filename.startsWith(context.packageName)
        }
        logFiles?.sortDescending()

        var candidateFile: File? = null
        logFiles?.firstOrNull()?.let {
            val firstLogFile = File(externalCacheDir, it)
            if (firstLogFile.length() < MAX_FILE_SIZE)
                candidateFile = firstLogFile
        }

        candidateFile?.let {
            file = it
        } ?: run {
            val fileNameTimeStamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            file = File(externalCacheDir, "${context.packageName}.$fileNameTimeStamp.log")
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
                else -> priority.toString()
            }

            val writer = FileWriter(file, true)
            writer.append(priorityText)
                    .append(" ")
                    .append(logTimeStamp)
                    .append(tag)
                    .append(message)
                    .append("\n")
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error while logging into file : $e")
        }

        super.log(priority, tag, message, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String? {

        return String.format(" %s.%s:%s ",
                super.createStackElementTag(element),
                element.methodName,
                element.lineNumber
        )
    }

    fun getFileName(): String = file.absolutePath

    companion object {

        private val LOG_TAG = FileLoggingTree::class.java.simpleName
        private const val MAX_FILE_SIZE: Long = 2000000 // 2MB
    }
}