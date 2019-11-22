package info.hannes.logcat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.hannes.logcat.base.LogBaseFragment
import info.hannes.timber.fileLoggingTree
import java.io.File
import java.util.*


class LogfileFragment : LogBaseFragment() {

    private lateinit var sourceFileName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sourceFileName = fileLoggingTree()!!.getFileName()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun readLogFile(): ArrayList<String> {
        var array: ArrayList<String> = arrayListOf()
        try {
            array = File(sourceFileName).useLines { ArrayList(it.toList()) }
        } catch (e: Exception) {
        }
        return array
    }

    override fun clearLog() {
        fileLoggingTree()?.let { tree ->
            tree.file.parent?.let { dir ->
                File(dir).listFiles()?.filter { it.name.endsWith(".log") }?.forEach { foundFile ->
                    if (foundFile.exists())
                        foundFile.delete()
                }
            }
        }
    }

    companion object {
        fun newInstance(targetFileName: String, searchHint: String, logMail: String = ""): LogfileFragment {
            val fragment = LogfileFragment()
            val args = Bundle()
            args.putString(FILE_NAME, targetFileName)
            args.putString(SEARCH_HINT, searchHint)
            args.putString(MAIL_LOGGER, logMail)
            fragment.arguments = args
            return fragment
        }
    }
}