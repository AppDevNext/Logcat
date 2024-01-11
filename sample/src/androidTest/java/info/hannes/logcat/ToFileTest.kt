package info.hannes.logcat

import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.screenshot.captureToBitmap
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moka.lib.assertions.MatchOperator
import com.moka.lib.assertions.RecyclerViewItemCountAssertion
import info.hannes.logcat.ui.LogfileActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToFileTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<LogfileActivity>()

    @get:Rule
    var nameRule = TestName()

    @Test
    fun logFileBasicTest() {
        onView(allOf(withContentDescription("Timber Logfile"),
                withParent(withId(info.hannes.logcat.ui.R.id.action_bar)),
                isDisplayed()))

        val recycler = onView(withId(info.hannes.logcat.ui.R.id.log_recycler))
        recycler.check(ViewAssertions.matches(isDisplayed()))

        recycler.check(RecyclerViewItemCountAssertion(2, MatchOperator.GREATER_EQUAL))

        // Screenshot is too fast. It generates black screens
        Thread.sleep(300)
        onView(isRoot())
            .captureToBitmap()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}")
    }

}
