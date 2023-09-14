package info.hannes.timber

import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import timber.log.Timber.Forest.tag

/* If you use old logcat, e.g Android Studio Electric Eel, you should use for newLogcat a false */
open class DebugFormatTree(private val newLogcat: Boolean = true) : Timber.DebugTree() {

    private var codeIdentifier = ""
    private var method = ""

    override fun createStackElementTag(element: StackTraceElement): String? {
        if (newLogcat) {
            method = String.format(
                "%s.%s()",
                // method is fully qualified only when class differs on filename otherwise it can be cropped on long lambda expressions
                super.createStackElementTag(element)?.replaceFirst(element.fileName.takeWhile { it != '.' }, ""),
                element.methodName
            )

            codeIdentifier = String.format(
                "(%s:%d)",
                element.fileName,
                element.lineNumber // format ensures line numbers have at least 3 places to align consecutive output from the same file
            )
            return "(${element.fileName}:${element.lineNumber})"
        } else
            return String.format(
                "(%s:%d) %s.%s()",
                element.fileName,
                element.lineNumber, // format ensures line numbers have at least 3 places to align consecutive output from the same file
                // method is fully qualified only when class differs on filename otherwise it can be cropped on long lambda expressions
                super.createStackElementTag(element)?.replaceFirst(element.fileName.takeWhile { it != '.' }, ""),
                element.methodName
            )
    }

    // if there is an JSON string, try to print out pretty
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var localMessage = message.trim()
        if (localMessage.startsWith("{") && localMessage.endsWith("}")) {
            try {
                val json = JSONObject(message)
                localMessage = json.toString(3)
            } catch (_: JSONException) {
            }
        }
        super.log(priority, tag, "$method: $localMessage", t)
    }
}
