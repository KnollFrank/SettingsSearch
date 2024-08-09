package de.KnollFrank.preferencesearch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.preferencesearch.Matchers.childAtPosition;
import static de.KnollFrank.preferencesearch.Matchers.recyclerViewHasItem;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PreferenceSearchExampleTest {

    @Rule
    public ActivityScenarioRule<PreferenceSearchExample> mActivityScenarioRule =
            new ActivityScenarioRule<>(PreferenceSearchExample.class);

    @Test
    public void shouldSearchAndFindPreference() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("fourth"), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring("Checkbox fourth file")));
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("dst preference"), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(1, click()));
        final String summaryOfFullyInstantiatedPreferenceFragment = "copied summary: summary of src preference";
        onView(summaryOfPreference()).check(matches(withText(summaryOfFullyInstantiatedPreferenceFragment)));
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        final String entryOfSomeListPreference = "Home";
        onView(searchButton()).perform(click());
        // When searching for an entry of a ListPreference
        onView(searchView()).perform(replaceText(entryOfSomeListPreference), closeSoftKeyboard());
        // Then this entry is displayed in search results
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(entryOfSomeListPreference)));
    }

    @Test
    public void shouldSearchAndNotFindInvisiblePreference() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("invisible"), closeSoftKeyboard());
        onView(searchResultsView()).check(doesNotExist());
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

    private static Matcher<View> searchResultsView() {
        return allOf(
                withId(androidx.preference.R.id.recycler_view),
                withParent(
                        allOf(
                                withId(android.R.id.list_container),
                                withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class)))),
                isDisplayed());
    }

    private static Matcher<View> hasSearchResultWithSubstring(final String substring) {
        return recyclerViewHasItem(hasDescendant(withSubstring(substring)));
    }

    private static Matcher<View> summaryOfPreference() {
        return allOf(
                withId(android.R.id.summary),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class))),
                isDisplayed());
    }
}
