package info.hannes.logcat

import android.graphics.Bitmap
import androidx.test.core.graphics.writeToTestStorage
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.captureToBitmap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import info.hannes.logcat.app.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var nameRule = TestName()

    @Before
    fun openDrawer() {
        val navMain = onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
        navMain.perform(click())
        onView(isRoot())
            .perform(captureToBitmap { bitmap: Bitmap -> bitmap.writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}") })
    }

    @Test
    fun navigationLogcatTest() {
        val menu = onView(withText("Logcat"))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        Thread.sleep(1000)
        onView(isRoot())
            .perform(captureToBitmap { bitmap: Bitmap -> bitmap.writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}") })
    }

    @Test
    fun navigationFileTest() {
        val menu = onView(withText("Timber Logfile"))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        onView(isRoot())
            .perform(captureToBitmap { bitmap: Bitmap -> bitmap.writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}") })
    }

    @Test
    fun navigationBothTest() {
        val menu = onView(withText(info.hannes.logcat.app.R.string.all_logfile))
        menu.check(matches(isDisplayed()))
        menu.perform(click())
        Thread.sleep(1000)
        onView(isRoot())
            .perform(captureToBitmap { bitmap: Bitmap -> bitmap.writeToTestStorage("${javaClass.simpleName}_${nameRule.methodName}") })
    }

}
