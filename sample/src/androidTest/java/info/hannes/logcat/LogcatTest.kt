package info.hannes.logcat

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.rule.GrantPermissionRule
import com.moka.lib.assertions.WaitingAssertion
import com.moka.utils.Screenshot
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class LogcatTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(LogcatActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Test
    fun basicLogcatTest() {
        onView(allOf(withContentDescription("Logcat"), withParent(withId(R.id.action_bar)), isDisplayed()))

        WaitingAssertion.checkAssertion(R.id.log_recycler, isDisplayed(), 1500)
        Screenshot.takeScreenshot("Step1")
        // TODO Moka 0.8 comes with assertRecyclerAdapterItemsCount()
        WaitingAssertion.assertAdapterMinimumItemsCount(R.id.log_recycler, 15, 1500)
        Screenshot.takeScreenshot("End")
    }

}
