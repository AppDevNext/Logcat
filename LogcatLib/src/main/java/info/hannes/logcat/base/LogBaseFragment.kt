package info.hannes.logcat.base

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import info.hannes.logcat.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


abstract class LogBaseFragment : Fragment() {

    private var verboseItem: MenuItem? = null
    private lateinit var logsRecycler: RecyclerView
    private var logListAdapter: LogListAdapter? = null
    private var searchView: SearchView? = null
    private val currentFilter = ""
    private var emailAddress: String = ""

    private var filename: String? = null
    private var searchHint: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_log, container, false)

        val layoutManager = LinearLayoutManager(context)
        logsRecycler = view.findViewById(R.id.log_recycler)
        logsRecycler.setHasFixedSize(true)
        logsRecycler.layoutManager = layoutManager

        if (activity!!.actionBar != null) {
            activity!!.actionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        if (savedInstanceState == null) {
            showLogContent()
        }

        setHasOptionsMenu(true)

        arguments?.let {
            filename = it.getString(FILE_NAME)
            searchHint = it.getString(SEARCH_HINT)
            it.getString(MAIL_LOGGER)?.let { address ->
                emailAddress = address
            }
        }

        return view
    }

    private fun showLogContent() = lifecycle.coroutineScope.launch(Dispatchers.Main) {
        showLoadingDialog()
        val logEntries = withContext(Dispatchers.Default) {
            readLogFile()
        }
        logListAdapter = LogListAdapter(logEntries, currentFilter)
        logsRecycler.adapter = logListAdapter
        logsRecycler.adapter?.itemCount?.minus(1)?.let { logsRecycler.scrollToPosition(it) }

        dismissLoadingDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater?.inflate(R.menu.menu_log, menu)
        verboseItem = menu?.findItem(R.id.menu_show_verbose)
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView?.queryHint = searchHint
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        val searchAutoComplete = searchView?.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)

        if (currentFilter != "") {
            searchAutoComplete?.setText(currentFilter)
            searchView?.isIconified = false
        } else {
            searchAutoComplete?.setText("")
            searchView?.isIconified = true
        }

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                setFilter2LogAdapter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                setFilter2LogAdapter(query)
                return true
            }
        }
        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                setFilter2LogAdapter("")
                return true  // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                return true  // Return true to expand action view
            }
        })

        searchView?.let {
            it.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            it.setIconifiedByDefault(true)
            it.setOnQueryTextListener(queryTextListener)
            if (currentFilter != "") {
                if (searchAutoComplete != null && searchItem != null) {
                    searchItem.expandActionView()
                    searchAutoComplete.setText(currentFilter)
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setFilter2LogAdapter(vararg filters: String) {
        // reset to verbose
        verboseItem?.let {
            if (!it.isChecked && filters.size == 1 && filters[0] != "")
                it.isChecked = true
        }
        logListAdapter?.setFilter(*filters)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var returnValue = true
        when (item!!.itemId) {
            R.id.menu_share -> filename?.let { fileName ->
                logListAdapter?.let {
                    sendLogContent(it.filterLogs, fileName)
                }
            }
            R.id.menu_clear -> {
                clearLog()
                showLogContent()
            }
            R.id.menu_show_verbose -> {
                item.isChecked = true
                stopSearchView()
                setFilter2LogAdapter("")
            }
            R.id.menu_show_debug -> {
                item.isChecked = true
                stopSearchView()
                setFilter2LogAdapter(ASSERT_LINE, ERROR_LINE, WARNING_LINE, INFO_LINE, DEBUG_LINE)
            }
            R.id.menu_show_info -> {
                item.isChecked = true
                stopSearchView()
                setFilter2LogAdapter(ASSERT_LINE, ERROR_LINE, WARNING_LINE, INFO_LINE)
            }
            R.id.menu_show_warning -> {
                item.isChecked = true
                stopSearchView()
                setFilter2LogAdapter(ASSERT_LINE, ERROR_LINE, WARNING_LINE)
            }
            R.id.menu_show_error -> {
                item.isChecked = true
                stopSearchView()
                setFilter2LogAdapter(ASSERT_LINE, ERROR_LINE)
            }
            else -> returnValue = super.onOptionsItemSelected(item)
        }
        return returnValue
    }

    abstract fun clearLog()

    private fun stopSearchView() {
        val searchAutoComplete = searchView?.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)

        searchAutoComplete?.setText("")
        searchView?.isIconified = true
    }

    private fun sendLogContent(filterLogs: List<String>, filename: String) {
        val logToSend = File(this@LogBaseFragment.activity?.externalCacheDir, filename)
        logToSend.writeText(filterLogs.joinToString("\n"))

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/zip"

        val uri = Uri.fromFile(logToSend)
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
        val subject = String.format(filename, getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.type = MAIL_ATTACHMENT_TYPE
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            // prevent from a "exposed beyond app through ClipData.Item.getUri()"
            // https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            startActivity(Intent.createChooser(intent, "$filename ..."))
        } catch (e: ActivityNotFoundException) {
            val snackBar = Snackbar.make(
                    this@LogBaseFragment.activity!!.findViewById(android.R.id.content),
                    R.string.log_send_no_app,
                    Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }

    }

    abstract fun readLogFile(): ArrayList<String>

    private fun showLoadingDialog() {
        showProgress++
        if (showProgress == 1) {
            var loadingDialog: LoadingDialog? = this@LogBaseFragment.activity!!.supportFragmentManager.findFragmentByTag(DIALOG_WAIT_TAG) as LoadingDialog?
            loadingDialog?.let {
                // Timber.i("showLogContent exists")
            } ?: run {
                loadingDialog = LoadingDialog.newInstance(false)
                val fm = this@LogBaseFragment.activity!!.supportFragmentManager
                val ft = fm.beginTransaction()
                loadingDialog?.show(ft, DIALOG_WAIT_TAG)
            }
        }
    }

    private fun dismissLoadingDialog() {
        showProgress--
        if (showProgress == 0) {
            this@LogBaseFragment.activity!!.supportFragmentManager.findFragmentByTag(DIALOG_WAIT_TAG)?.let {
                val loading = it as LoadingDialog
                loading.dismiss()
            }
        }
    }

    companion object {

        private const val MAIL_ATTACHMENT_TYPE = "text/plain"

        private const val DIALOG_WAIT_TAG = "DIALOG_WAIT"

        private var showProgress = 0
        const val FILE_NAME = "targetFilename"
        const val SEARCH_HINT = "search_hint"
        const val MAIL_LOGGER = "mail_logger"
        const val VERBOSE_LINE = "V: "
        const val DEBUG_LINE = "D: "
        const val INFO_LINE = "I: "
        const val WARNING_LINE = "W: "
        const val ERROR_LINE = "E: "
        const val ASSERT_LINE = "A: "
    }

}