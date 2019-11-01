package info.hannes.timber

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
@SuppressLint("LogNotTimber")
class FileLoggingTree(externalCacheDir: File, context: Context? = null, filename: String = UUID.randomUUID().toString()) : DebugTree() {

    var file: File
        private set

    init {
        if (!externalCacheDir.exists()) {
            if (!externalCacheDir.mkdirs())
                Log.e("FileLoggingTree", "couldn't create ${externalCacheDir.absoluteFile}")
        }
        val fileNameTimeStamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        file = if (context != null) {
            File(externalCacheDir, "${context.packageName}.$fileNameTimeStamp.log")
        } else {
            File(externalCacheDir, "$filename.$fileNameTimeStamp.log")
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
            // Log to prevent an endless loop
            Log.e(LOG_TAG, "Error while logging into file : $e")
        }

        super.log(priority, tag, message, t)
    }

    fun getFileName(): String = file.absolutePath

    companion object {

        private val LOG_TAG = FileLoggingTree::class.java.simpleName
    }
}