package com.example.assignment2

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest3 {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest3() {

        // Click the Rent button
        onView(allOf(withId(R.id.btn_rent), withText("Rent"), isDisplayed()))
            .perform(click())

        // Input duration
        onView(withId(R.id.duration))
            .perform(replaceText("90"), closeSoftKeyboard())

        // Press back to return to the previous screen
        pressBack()

        // Select chips
        onView(allOf(withId(R.id.chip2), withText("Stand"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.chip3), withText("Extra String"), isDisplayed()))
            .perform(click())

        // Click the Save button
        onView(allOf(withId(R.id.btn_save), withText("Save"), isDisplayed()))
            .perform(click())

        // Click the Save button again if required (check if this is intentional)
        onView(allOf(withId(R.id.btn_save), withText("Save"), isDisplayed()))
            .perform(click())

        // Verify the total cost
        onView(allOf(withId(R.id.total_cost), withText("2250"), isDisplayed()))
            .check(matches(withText("2250")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
