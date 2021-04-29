package info.hannes.logcat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import info.hannes.logcat.base.LogBaseFragment
import info.hannes.timber.FileLoggingTree
import info.hannes.timber.fileLoggingTree
import java.io.File

class LogfileFragment : LogBaseFragment(), Observer<Event<String>> {

    private var sourceFileName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sourceFileName = fileLoggingTree()?.getFileName()
        val view = super.onCreateView(inflater, container, savedInstanceState)
        FileLoggingTree.lastLogEntry.observe(viewLifecycleOwner, this)
        return view
    }

    override fun readLogFile(): MutableList<String> {
        var array = mutableListOf<String>()
        try {
            sourceFileName?.let {
                array = File(it).useLines { it.toMutableList() }
            }
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

    override fun onChanged(line: Event<String>?) {
        line?.getContentIfNotHandled()?.let {
            logListAdapter?.addLine(it)
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
