package info.hannes.logcat

import androidx.test.core.app.takeScreenshot
import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.moka.lib.assertions.MatchOperator
import com.moka.lib.assertions.RecyclerViewItemCountAssertion
import info.hannes.logcat.ui.LogfileActivity
import info.hannes.logcat.utils.RecyclerViewItemDuplicateAssertion
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ResumeTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<LogfileActivity>()

    @get:Rule
    var nameRule = TestName()

    @Ignore("It doesn't work")
    @Test
    fun checkForDuplicateAfterPressRecentApps() {
        // Might be a good idea to initialize it somewhere else
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        takeScreenshot()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-1-start")
        uiDevice.pressRecentApps()
        Thread.sleep(WAIT)
        takeScreenshot()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-2")

        uiDevice.pressRecentApps()
        Thread.sleep(WAIT)
        takeScreenshot()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-3")

        // final checks
        val recycler = Espresso.onView(ViewMatchers.withId(info.hannes.logcat.ui.R.id.log_recycler))
        recycler.check(RecyclerViewItemDuplicateAssertion())
        takeScreenshot()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-5")

        recycler.check(RecyclerViewItemCountAssertion(2, MatchOperator.GREATER_EQUAL))
        takeScreenshot()
            .writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}-6")
    }

    companion object {
        const val WAIT = 300L
    }
}
