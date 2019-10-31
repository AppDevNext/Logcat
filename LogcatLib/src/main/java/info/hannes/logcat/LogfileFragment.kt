package info.hannes.logcat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.hannes.logcat.base.LogBaseFragment
import info.hannes.timber.FileLoggingTree
import info.hannes.timber.fileLoggingTree
import timber.log.Timber
import java.io.File
import java.util.*


class LogfileFragment : LogBaseFragment() {

    private lateinit var sourceFileName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sourceFileName = Timber.forest().fileLoggingTree()!!.getFileName()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun readLogFile(): ArrayList<String> {
        return File(sourceFileName).useLines { ArrayList(it.toList()) }
    }

    override fun clearLog() {
        Timber.forest().fileLoggingTree()?.let {
            val logfile= it.file
            Timber.forest().remove(it)
            if (logfile.exists())
                logfile.delete()

            requireContext().externalCacheDir?.let {
                Timber.plant(FileLoggingTree(it, requireContext()))
            }
        }

        Timber.forest().fileLoggingTree()!!.getFileName()
        File(sourceFileName)
    }

    companion object {
        fun newInstance(targetFileName: String, searchHint: String): LogfileFragment {
            val fragment = LogfileFragment()
            val args = Bundle()
            args.putString(FILE_NAME, targetFileName)
            args.putString(SEARCH_HINT, searchHint)
            fragment.arguments = args
            return fragment
        }
    }
}