package info.hannes.logcat

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.rule.GrantPermissionRule
import com.moka.lib.assertions.WaitingAssertion
import com.moka.utils.Screenshot
import info.hannes.logcat.utils.MatchOperator
import info.hannes.logcat.utils.RecyclerViewItemCountAssertion
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class AllFragmentsTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(BothLogActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Test
    fun basicTest() {
        onView(
            allOf(
                withContentDescription("All log"),
                withParent(withId(R.id.action_bar)),
                isDisplayed()
            )
        )

        onView(withText("Logcat")).check(ViewAssertions.matches(isDisplayed()))
        onView(withText("Logfile")).check(ViewAssertions.matches(isDisplayed()))

        WaitingAssertion.checkAssertion(R.id.log_recycler, isDisplayed(), 1500)
        onView(allOf(withId(R.id.log_recycler), isDisplayed()))
            .check(RecyclerViewItemCountAssertion(2, MatchOperator.GRATER_EQUAL))

        Screenshot.takeScreenshot("End")
    }

}
