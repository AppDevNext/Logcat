package info.hannes.logcat.utils

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert


class RecyclerViewItemDuplicateAssertion : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let {
            throw it
        }
        val recyclerView = view as RecyclerView
        val count = recyclerView.childCount
        var i = 0
        var previousText = ""
        while (i < count) {
            val holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val currentText = (holder.itemView as AppCompatTextView).text.toString().trim()
            if (previousText == currentText)
                MatcherAssert.assertThat(currentText, CoreMatchers.`is`("Line $i is a duplicate"))

            previousText = currentText
            ++i
        }
    }
}
