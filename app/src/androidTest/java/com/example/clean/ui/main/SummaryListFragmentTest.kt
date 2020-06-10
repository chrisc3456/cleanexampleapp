package com.example.clean.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.clean.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SummaryListFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun summaryListFragmentTest() {
        val textView = onView(
            allOf(
                withId(R.id.textViewSummaryTitle),
                withText("Lorem ipsum dolor sit amet, consectetur adipiscing elit"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Lorem ipsum dolor sit amet, consectetur adipiscing elit")))

        val textView2 = onView(
            allOf(
                withId(R.id.textViewSummaryExcerpt),
                withText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam id molestie augue, in molestie turpis. Fusce elit ipsum, volutpat vitae justo at, sodales aliquam risus. Donec tristique mollis venenatis. Phasellus."),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam id molestie augue, in molestie turpis. Fusce elit ipsum, volutpat vitae justo at, sodales aliquam risus. Donec tristique mollis venenatis. Phasellus.")))

        val textView3 = onView(
            allOf(
                withId(R.id.textViewSummaryDate), withText("2020-05-00T12:34:56"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("2020-05-00T12:34:56")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
