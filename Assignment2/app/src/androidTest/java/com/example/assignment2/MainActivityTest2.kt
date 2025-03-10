package com.example.assignment2


import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        // Click the Rent button
        onView(allOf(withId(R.id.btn_rent), withText("Rent"), isDisplayed()))
            .perform(click())

        // Ensure that RentActivity is displayed
        onView(withId(R.id.duration)).check(matches(isDisplayed()))

        // Input duration in RentActivity
        onView(withId(R.id.duration)).perform(replaceText("13"), closeSoftKeyboard())

        // Click Save button in RentActivity
        onView(allOf(withId(R.id.btn_save), withText("Save"), isDisplayed()))
            .perform(click())

        // Ensure MainActivity is displayed again
        onView(withId(R.id.credit)).check(matches(isDisplayed()))

        // Check the credit text value
        onView(allOf(withId(R.id.credit), withText("805"), isDisplayed()))
            .check(matches(withText("805")))
    }
}
