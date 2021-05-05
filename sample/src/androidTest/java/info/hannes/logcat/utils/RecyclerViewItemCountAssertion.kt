package info.hannes.logcat.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

class RecyclerViewItemCountAssertion(private val expectedCount: Int, private val operator: MatchOperator) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let {
            throw it
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        when (operator) {
            MatchOperator.IS -> MatcherAssert.assertThat(adapter!!.itemCount, Matchers.`is`(expectedCount))
            MatchOperator.LESS -> MatcherAssert.assertThat(adapter!!.itemCount, Matchers.lessThan(expectedCount))
            MatchOperator.LESS_EQUAL -> MatcherAssert.assertThat(adapter!!.itemCount, Matchers.lessThanOrEqualTo(expectedCount))
            MatchOperator.GRATER -> MatcherAssert.assertThat(adapter!!.itemCount, Matchers.greaterThan(expectedCount))
            MatchOperator.GRATER_EQUAL -> MatcherAssert.assertThat(adapter!!.itemCount, Matchers.greaterThanOrEqualTo(expectedCount))
        }

    }
}
