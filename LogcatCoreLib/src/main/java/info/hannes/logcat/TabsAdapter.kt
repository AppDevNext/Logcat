package info.hannes.logcat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class TabsAdapter(activity: FragmentActivity, private val tabHost: TabHost, private val viewPager: ViewPager) :
        FragmentPagerAdapter(activity.supportFragmentManager),
        TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener {

    private val context: Context
    private val fragments = mutableListOf<Fragment>()

    private class DummyTabFactory(private val contextView: Context) : TabHost.TabContentFactory {

        override fun createTabContent(tag: String): View {
            val view = View(contextView)
            view.minimumWidth = 0
            view.minimumHeight = 0
            return view
        }
    }

    init {
        context = activity
        tabHost.setOnTabChangedListener(this)
        viewPager.adapter = this
        viewPager.addOnPageChangeListener(this)
    }

    fun addTab(tabSpec: TabHost.TabSpec, fragment: Fragment) {
        tabSpec.setContent(DummyTabFactory(context))
        tabHost.addTab(tabSpec)
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun onTabChanged(tabId: String) {
        val position = tabHost.currentTab
        viewPager.currentItem = position
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) {
        // Unfortunately when TabHost changes the current tab, it kindly also takes care of putting focus on it when not in touch mode.
        // This hack tries to prevent this from pulling focus out of our ViewPager.
        val widget = tabHost.tabWidget
        val oldFocusability = widget.descendantFocusability
        widget.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        try {
            tabHost.currentTab = position
        } catch (ignored: Exception) {
        }

        widget.descendantFocusability = oldFocusability
    }

    override fun onPageScrollStateChanged(state: Int) = Unit
}
