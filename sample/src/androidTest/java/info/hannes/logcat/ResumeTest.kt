package info.hannes.logcat

import android.Manifest
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import info.hannes.logcat.utils.MatchOperator
import info.hannes.logcat.utils.RecyclerViewItemCountAssertion
import info.hannes.logcat.utils.RecyclerViewItemDuplicateAssertion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ResumeTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(LogfileActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    @Test
    fun checkForDuplicateAfterPressRecentApps() {
        // Might be a good idea to initialize it somewhere else
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.pressRecentApps()
        Thread.sleep(WAIT)
        uiDevice.pressRecentApps()
        Thread.sleep(WAIT)

        val recycler = Espresso.onView(ViewMatchers.withId(R.id.log_recycler))
        recycler.check(RecyclerViewItemDuplicateAssertion())

        recycler.check(RecyclerViewItemCountAssertion(2, MatchOperator.GRATER_EQUAL))
    }

    companion object {
        const val WAIT = 300L
    }
}
