package info.hannes.logcat

import android.graphics.Bitmap
import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.captureToBitmap
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import info.hannes.logcat.ui.BothLogActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AllFragmentsTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<BothLogActivity>()

    @get:Rule
    var nameRule = TestName()

    @Test
    fun basicTest() {
        onView(allOf(withContentDescription("All log"),
                withParent(withId(info.hannes.logcat.ui.R.id.action_bar)),
                isDisplayed()))

        onView(withText("Logcat")).check(ViewAssertions.matches(isDisplayed()))
        onView(withText("Logfile")).check(ViewAssertions.matches(isDisplayed()))

        onView(isRoot())
            .perform(captureToBitmap { bitmap: Bitmap -> bitmap.writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}") })
    }

}
