package info.hannes.logcat

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.rule.GrantPermissionRule
import com.moka.utils.Screenshot
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainActivityTest {

    @get:Rule
    val mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    @Before
    fun openDrawer() {
        val navMain = onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
        navMain.perform(click())
        Screenshot.takeScreenshot("End")
    }

    @Test
    fun navigationLogcatTest() {
        val menu = onView(withText("Logcat"))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        Thread.sleep(1000)
        Screenshot.takeScreenshot("End")
    }

    @Test
    fun navigationFileTest() {
        val menu = onView(withText("Timber Logfile"))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        Screenshot.takeScreenshot("End")
    }

    @Test
    fun navigationBothTest() {
        val menu = onView(withText(info.hannes.logcat.sample.R.string.all_logfile))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        Thread.sleep(1000)
        Screenshot.takeScreenshot("End")
    }

}
