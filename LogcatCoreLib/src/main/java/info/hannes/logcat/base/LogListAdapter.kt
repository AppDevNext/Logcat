package info.hannes.logcat.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.hannes.logcat.R
import java.util.*

class LogListAdapter(private var completeLogs: MutableList<String>, filter: String) : RecyclerView.Adapter<LogListAdapter.LogViewHolder>() {

    private lateinit var currentFilter: Array<out String>
    var filterLogs = mutableListOf<String>()

    init {
        setFilter(filter)
    }

    fun setItems(newItems: MutableList<String>) {
        completeLogs = newItems
        setFilter(*currentFilter)
        notifyDataSetChanged()
    }

    fun setFilter(vararg filters: String) {
        currentFilter = filters
        filterLogs = Collections.synchronizedList(completeLogs).filter { line ->
            var include = false
            for (filter in filters) {
                if (filter.length == 3 && filter.takeLast(2) == ": ") { // eg 'E: '
                    if (!include && (line.contains(" $filter", false) || line.startsWith(filter, false)))
                        include = true
                } else {
                    if (!include && line.contains(filter, true))
                        include = true
                }
            }
            include
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun addLine(line: String) {
        completeLogs.add(completeLogs.size, line)

        currentFilter.let {
            for (filter in it) {
                if (line.contains(filter)) {
                    filterLogs.add(filterLogs.size, line)
                    notifyItemInserted(filterLogs.size - 1)
                }
            }
        }
    }

    /**
     * Create the view for each log in the list
     *
     * @param viewGroup
     * @param i
     * @return
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LogViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_log, viewGroup, false)
        return LogViewHolder(view)
    }

    /**
     * Fill in each log in the list
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val text = filterLogs[position]
        holder.logContent.let {
            it.text = text

            when {
                text.contains(" ${LogBaseFragment.ERROR_LINE}") || text.startsWith(LogBaseFragment.ERROR_LINE) -> {
                    getAttrColorStateList(it.context, R.attr.colorErrorLine) ?: ColorStateList.valueOf(Color.RED)
                }
                text.contains(" ${LogBaseFragment.ASSERT_LINE}") || text.startsWith(LogBaseFragment.ASSERT_LINE) -> {
                    getAttrColorStateList(it.context, R.attr.colorAssertLine) ?: ColorStateList.valueOf(Color.RED)
                }
                text.contains(" ${LogBaseFragment.INFO_LINE}") || text.startsWith(LogBaseFragment.INFO_LINE) -> {
                    getAttrColorStateList(it.context, R.attr.colorInfoLine) ?: getAttrColorStateList(it.context, android.R.attr.textColorPrimary)
                }
                text.contains(" ${LogBaseFragment.WARNING_LINE}") || text.startsWith(LogBaseFragment.WARNING_LINE) -> {
                    getAttrColorStateList(it.context, R.attr.colorWarningLine) ?: ColorStateList.valueOf(Color.MAGENTA)
                }
                text.contains(" ${LogBaseFragment.VERBOSE_LINE}") || text.startsWith(LogBaseFragment.VERBOSE_LINE) -> {
                    getAttrColorStateList(it.context, R.attr.colorVerboseLine) ?: ColorStateList.valueOf(Color.GRAY)
                }
                else -> {
                    getAttrColorStateList(it.context, R.attr.colorDebugLine) ?: getAttrColorStateList(it.context, android.R.attr.textColorSecondary)
                }
            }?.let { colors: ColorStateList -> it.setTextColor(colors) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_log
    }

    override fun getItemCount(): Int = filterLogs.size

    /**
     * Define the view for each log in the list
     */
    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val logContent: TextView = view.findViewById(R.id.logLine)
    }

    companion object {

        /**
         * Retrieve the ColorStateList for the given attribute. The value may be either a single solid
         * color or a reference to a color or complex [android.content.res.ColorStateList]
         * description.
         *
         * @param context
         * @param attr
         * @return
         */
        fun getAttrColorStateList(context: Context, attr: Int): ColorStateList? = try {
            val ta = context.obtainStyledAttributes(intArrayOf(attr))
            val colorStateList = ta.getColorStateList(0)
            ta.recycle()
            colorStateList
        } catch (e: Resources.NotFoundException) {
            null
        }
    }
}
