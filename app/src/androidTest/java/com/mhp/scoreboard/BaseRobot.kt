package com.mhp.scoreboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.mockk.MockKAnnotations
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.koin.core.context.stopKoin

abstract class BaseRobot {

    lateinit var navController: TestNavHostController

    /**
     * Will be called for each test to start the test setup
     */
    fun setup() {
        MockKAnnotations.init(this)
        setupTestComponents()
    }

    /**
     * Setting up first the view model(s) and then the fragment or activity scenario
     * for each screen robot
     */
    abstract fun setupTestComponents()

    /**
     * Is Called after each test to clear Idling Resource and to stop the
     * current StandAlone Koin application
     */
    @Suppress("unused")
    fun clearTestResources() {
        stopKoin()
    }

    /**
     * Setting up the fragment for testing (It is not necessary to initialize an activity)
     */
    protected inline fun <reified T : Fragment> setupFragmentScenario(bundle: Bundle? = null): FragmentScenario<T> {
        //Setting the theme inside this would break the roboelectric tests
        // Create a TestNavHostController
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)
        val scenario: FragmentScenario<T> = launchFragmentInContainer(bundle, themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        return scenario
    }


    fun fillEditText(resId: Int, text: String): ViewInteraction =
        onView(withId(resId)).perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard())

    fun clickButton(resId: Int): ViewInteraction = onView((withId(resId))).perform(click())

    fun checkButtonEnabled(resId: Int): ViewInteraction = onView((withId(resId))).check(matches(isEnabled()))

    fun checkButtonDisabled(resId: Int): ViewInteraction = onView((withId(resId))).check(matches(not(isEnabled())))

    fun textView(resId: Int): ViewInteraction = onView(withId(resId))

    fun matchText(viewInteraction: ViewInteraction, text: String): ViewInteraction = viewInteraction
        .check(matches(ViewMatchers.withText(text)))

    fun checkRecyclerViewNumberOfEntries(recyclerView: Int, numberOfEntries: Int) {
        onView(withId(recyclerView))
            .check(RecyclerViewItemCountAssertion(numberOfEntries))
    }

    fun matchText(resId: Int, text: String): ViewInteraction = matchText(textView(resId), text)

    fun clickOnItemInRecyclerView(recyclerView: Int, position: Int) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
        )
    }

    fun clickListItem(listRes: Int, position: Int) {
        onData(anything())
            .inAdapterView(allOf(withId(listRes)))
            .atPosition(position).perform(click())
    }

    fun checkTextInsideRecyclerView(recyclerView: Int, position: Int, element: Int, text: String) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                position
            )
        ).check(
            matches(
                withViewAtPosition(
                    position,
                    ViewMatchers.hasDescendant(allOf(withId(element), ViewMatchers.withText(text)))
                )
            )
        )
    }

    //for matching element of recyclerview
    private fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    inner class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            ViewMatchers.assertThat(adapter!!.itemCount, CoreMatchers.`is`(expectedCount))
        }
    }
}

inline fun <T : BaseRobot> T.check(func: T.() -> Unit) {
    try {
        this.apply {
            setup()
            func()
        }
    } finally {
        clearTestResources()
    }
}
