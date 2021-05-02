package info.hannes.logcat

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.rule.GrantPermissionRule
import com.moka.utils.Screenshot
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ToFileTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(LogfileActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    @Test
    fun logFileBasicTest() {
        onView(allOf(withContentDescription("Timber Logfile"),
                withParent(withId(R.id.action_bar)),
                isDisplayed()))

        val recycler = onView(withId(R.id.log_recycler))
        recycler.check(ViewAssertions.matches(isDisplayed()))
        Screenshot.takeScreenshot("End")
    }

}
