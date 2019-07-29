package info.hannes.logcat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import info.hannes.timber.FileLoggingTree

/**
 * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
 */
class BothLogsFragment : Fragment() {

    var sourcefilename = ""
    var filename = ""
    var searchHintLogcat = ""
    var searchHintLogfile = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.fragment_both_logs, null)

        val mTabHost = view.findViewById<TabHost>(android.R.id.tabhost)
        mTabHost.setup()

        arguments?.let {
            sourcefilename = it.getString(SOURCE_FILE_NAME, "")
            filename = it.getString(FILE_NAME, "")
            searchHintLogcat = it.getString(SEARCH_HINT_LOGCAT, "")
            searchHintLogfile = it.getString(SEARCH_HINT_LOGFILE, "")
        }

        val mViewPager = view.findViewById<ViewPager>(R.id.pager)

        // If non-null, this is the current filter the user has provided.
        val mTabsAdapter = TabsAdapter(requireActivity(), mTabHost, mViewPager)

        val logcatFragment = LogcatFragment.newInstance(sourcefilename, searchHintLogcat)

        val logfileFragment = LogfileFragment.newInstance(
                FileLoggingTree.getFileName(),
                filename,
                searchHintLogfile
        )

        mTabsAdapter.addTab(mTabHost.newTabSpec("nameC").setIndicator("Logcat"), logcatFragment)
        mTabsAdapter.addTab(mTabHost.newTabSpec("nameF").setIndicator("Logfile"), logfileFragment)

        if (savedInstanceState != null) {
            try {
                mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"))
            } catch (ignored: Exception) {
            }

        }
        return view
    }

    companion object {

        private const val SOURCE_FILE_NAME = "source file name"
        private const val FILE_NAME = "file name"
        private const val SEARCH_HINT_LOGFILE = "searchHintfile"
        private const val SEARCH_HINT_LOGCAT = "searchHintlogcat"

        fun newInstance(sourceFileName: String, targetFileName: String, searchHintLogfile: String, searchHintLogcat: String): BothLogsFragment {
            val fragment = BothLogsFragment()
            val args = Bundle()
            args.putString(SOURCE_FILE_NAME, sourceFileName)
            args.putString(FILE_NAME, targetFileName)
            args.putString(SEARCH_HINT_LOGFILE, searchHintLogfile)
            args.putString(SEARCH_HINT_LOGCAT, searchHintLogcat)
            fragment.arguments = args
            return fragment
        }

    }

}
