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
        onView(allOf(withId(R.id.btn_next), withText("Next"), isDisplayed()))
            .perform(click())
        // Click the Rent button
        onView(allOf(withId(R.id.btn_rent), withText("Rent"), isDisplayed()))
            .perform(click())

        // Ensure that RentActivity is displayed
        onView(withId(R.id.duration)).check(matches(isDisplayed()))

        // Input duration in RentActivity
        onView(withId(R.id.duration)).perform(replaceText("9"), closeSoftKeyboard())

        // Click Save button in RentActivity
        onView(allOf(withId(R.id.btn_save), withText("Save"), isDisplayed()))
            .perform(click())

        // Ensure MainActivity is displayed again
        onView(withId(R.id.credit)).check(matches(isDisplayed()))

        // Check the credit text value
        onView(allOf(withId(R.id.credit), withText("415"), isDisplayed()))
            .check(matches(withText("415")))
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
