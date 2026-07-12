package info.hannes.logcat.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = mutableListOf<Pair<String, Fragment>>()

    fun addTab(title: String, fragment: Fragment) {
        fragments.add(title to fragment)
        notifyItemInserted(fragments.size - 1)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].second

    fun getTabTitle(position: Int): String = fragments[position].first
}
