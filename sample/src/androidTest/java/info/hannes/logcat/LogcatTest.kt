package info.hannes.logcat

import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.screenshot.captureToBitmap
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.moka.lib.assertions.MatchOperator
import com.moka.lib.assertions.WaitingAssertion
import info.hannes.logcat.ui.LogcatActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class LogcatTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<LogcatActivity>()

    @get:Rule
    var nameRule = TestName()

    @Test
    fun basicLogcatTest() {
        onView(allOf(withContentDescription("Logcat"), withParent(withId(info.hannes.logcat.ui.R.id.action_bar)), isDisplayed()))

        WaitingAssertion.checkAssertion(info.hannes.logcat.ui.R.id.log_recycler, isDisplayed(), 1500)
        onView(isRoot())
            .captureToBitmap()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-Step1")
        WaitingAssertion.assertRecyclerAdapterItemsCount(info.hannes.logcat.ui.R.id.log_recycler, 15, MatchOperator.GREATER, 1500)
        onView(isRoot())
            .captureToBitmap()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-End")
    }

}
