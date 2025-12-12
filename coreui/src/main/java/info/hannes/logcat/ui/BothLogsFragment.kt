package info.hannes.logcat.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import info.hannes.logcat.base.LogBaseFragment.Companion.MAIL_LOGGER

/**
 * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
 */
class BothLogsFragment : Fragment() {

    private var targetFilename = ""
    private var searchHintLogcat = ""
    private var searchHintLogfile = ""
    private var emailAddress = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        @SuppressLint("InflateParams")
        val view = inflater.inflate(R.layout.fragment_both_logs, null)

        val mTabHost = view.findViewById<TabHost>(android.R.id.tabhost)
        mTabHost.setup()

        arguments?.let {
            targetFilename = it.getString(TARGET_FILE_NAME, "")
            searchHintLogcat = it.getString(SEARCH_HINT_LOGCAT, "")
            searchHintLogfile = it.getString(SEARCH_HINT_LOGFILE, "")
            it.getString(MAIL_LOGGER)?.let { address ->
                emailAddress = address
            }
        }

        val mViewPager = view.findViewById<ViewPager>(R.id.pager)

        // If non-null, this is the current filter the user has provided.
        val mTabsAdapter = TabsAdapter(requireActivity(), mTabHost, mViewPager)

        val logcatFragment = LogcatFragment.newInstance(targetFilename, searchHintLogcat, emailAddress)

        val logfileFragment = LogfileFragment.newInstance(
            targetFilename,
            searchHintLogfile,
            emailAddress
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

        private const val TARGET_FILE_NAME = "file name"
        private const val SEARCH_HINT_LOGFILE = "searchHintfile"
        private const val SEARCH_HINT_LOGCAT = "searchHintlogcat"

        fun newInstance(targetFileName: String, searchHintLogfile: String, searchHintLogcat: String, logMail: String = ""): BothLogsFragment {
            val fragment = BothLogsFragment()
            val args = Bundle()
            args.putString(TARGET_FILE_NAME, targetFileName)
            args.putString(SEARCH_HINT_LOGFILE, searchHintLogfile)
            args.putString(SEARCH_HINT_LOGCAT, searchHintLogcat)
            args.putString(MAIL_LOGGER, logMail)
            fragment.arguments = args
            return fragment
        }
    }
}
