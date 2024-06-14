package de.KnollFrank.preferencesearch;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchViewExampleTest {

    @Rule
    public ActivityScenarioRule<SearchViewExample> mActivityScenarioRule =
            new ActivityScenarioRule<>(SearchViewExample.class);

    @Test
    public void shouldSearchAndFindPreferences() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("fourth"), closeSoftKeyboard());
        onView(checkboxInSearchResults()).check(matches(withText("Checkbox fourth file")));
    }

    private static Matcher<View> searchButton() {
        return allOf(
                withId(R.id.search_action),
                withContentDescription("title"),
                childAtPosition(
                        childAtPosition(
                                withId(androidx.appcompat.R.id.action_bar),
                                1),
                        0),
                isDisplayed());
    }

    private static Matcher<View> searchView() {
        return allOf(
                withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                childAtPosition(
                        allOf(
                                withClassName(is("android.widget.LinearLayout")),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1)),
                        0),
                isDisplayed());
    }

    private static Matcher<View> checkboxInSearchResults() {
        return allOf(
                withId(android.R.id.title),
                withText("Checkbox fourth file"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class))),
                isDisplayed());
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final View view) {
                final ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
