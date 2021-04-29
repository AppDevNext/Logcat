package info.hannes.logcat.base

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
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

abstract class LogBaseFragment : Fragment() {

    private var verboseItem: MenuItem? = null
    private lateinit var logsRecycler: RecyclerView
    protected var logListAdapter: LogListAdapter? = null
    private var searchView: SearchView? = null
    private val currentFilter = ""
    private var emailAddress: String = ""

    private var filename: String? = null
    private var searchHint: String? = null
    protected var showLive: Boolean = false
        set(value) {
            field = value
            requireActivity().invalidateOptionsMenu()
        }
    private var live: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_log, container, false)

        logsRecycler = view.findViewById<RecyclerView>(R.id.log_recycler).also {
            it.layoutManager = LinearLayoutManager(it.context)
            it.recycledViewPool.setMaxRecycledViews(R.layout.item_log, DEFAULT_MAX_SCRAP)
        }
        // empty adapter to avoid "E/RecyclerView﹕ No adapter attached; skipping layou..."
        logListAdapter = LogListAdapter(mutableListOf(), currentFilter)
        logsRecycler.adapter = logListAdapter

        setFilter2LogAdapter("")

        showLogContent()

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

    private fun showLogContent() {
        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            val logEntries = withContext(Dispatchers.Default) {
                readLogFile()
            }
            logListAdapter?.setItems(logEntries)
            logListAdapter?.itemCount?.minus(1)?.let { logsRecycler.scrollToPosition(it) }

            if (live) {
                Handler(Looper.getMainLooper()).postDelayed({ showLogContent() }, 1000)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_log, menu)
        verboseItem = menu.findItem(R.id.menu_show_verbose)
        val searchItem: MenuItem? = menu.findItem(R.id.menu_search)
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }

        val searchAutoComplete = searchView?.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)

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
            it.setIconifiedByDefault(true)
            it.setMaxWidth(Int.MAX_VALUE)
            it.setOnQueryTextListener(queryTextListener)
            it.setQueryHint(searchHint)
            it.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            if (currentFilter != "") {
                searchItem?.expandActionView()
                searchAutoComplete?.setText(currentFilter)
                it.isIconified = false
            } else {
                searchAutoComplete?.setText("")
                it.isIconified = true
            }
        }

        val switch = menu.findItem(R.id.menu_live).actionView as CompoundButton
        switch.setOnCheckedChangeListener { _, isChecked ->
            live = isChecked
            if (live)
                showLogContent()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_live)?.isVisible = showLive
    }

    private fun setFilter2LogAdapter(vararg filters: String) {
        // reset to verbose
        verboseItem?.let {
            if (!it.isChecked && filters.size == 1 && filters[0] != "")
                it.isChecked = true
        }
        logListAdapter?.setFilter(*filters)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnValue = true
        when (item.itemId) {
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
        val logToSend = File(requireActivity().externalCacheDir, filename)
        logToSend.writeText(filterLogs.joinToString("\n"))

        val intent = Intent(Intent.ACTION_SEND)

        val logsUri = FileProvider.getUriForFile(requireContext(), context?.applicationContext?.packageName + ".provider", logToSend)

        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
        val subject = String.format(filename, getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.type = MAIL_ATTACHMENT_TYPE
        intent.putExtra(Intent.EXTRA_STREAM, logsUri)
        try {
            // prevent from a "exposed beyond app through ClipData.Item.getUri()"
            // https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            startActivity(Intent.createChooser(intent, "$filename ..."))
        } catch (e: ActivityNotFoundException) {
            val snackBar = Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.log_send_no_app,
                    Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }
    }

    abstract fun readLogFile(): MutableList<String>

    companion object {

        private const val DEFAULT_MAX_SCRAP = 5 * 10

        private const val MAIL_ATTACHMENT_TYPE = "text/plain"

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
