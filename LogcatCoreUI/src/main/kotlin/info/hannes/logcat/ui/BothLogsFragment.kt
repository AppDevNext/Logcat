package info.hannes.logcat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        val view = inflater.inflate(R.layout.fragment_both_logs, container, false)

        arguments?.let {
            targetFilename = it.getString(TARGET_FILE_NAME, "")
            searchHintLogcat = it.getString(SEARCH_HINT_LOGCAT, "")
            searchHintLogfile = it.getString(SEARCH_HINT_LOGFILE, "")
            it.getString(MAIL_LOGGER)?.let { address ->
                emailAddress = address
            }
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)

        val adapter = TabsAdapter(requireActivity())
        adapter.addTab("Logcat", LogcatFragment.newInstance(targetFilename, searchHintLogcat, emailAddress))
        adapter.addTab("Logfile", LogfileFragment.newInstance(targetFilename, searchHintLogfile, emailAddress))
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        if (savedInstanceState != null) {
            viewPager.currentItem = savedInstanceState.getInt(KEY_CURRENT_TAB, 0)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.findViewById<ViewPager2>(R.id.pager)?.let {
            outState.putInt(KEY_CURRENT_TAB, it.currentItem)
        }
    }

    companion object {

        private const val TARGET_FILE_NAME = "file name"
        private const val SEARCH_HINT_LOGFILE = "searchHintfile"
        private const val SEARCH_HINT_LOGCAT = "searchHintlogcat"
        private const val KEY_CURRENT_TAB = "current_tab"

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
