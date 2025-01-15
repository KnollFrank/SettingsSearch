package de.KnollFrank.settingssearch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.settingssearch.Matchers.childAtPosition;
import static de.KnollFrank.settingssearch.Matchers.recyclerViewHasItem;
import static de.KnollFrank.settingssearch.Matchers.recyclerViewHasItemCount;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

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
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithExtras() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(0, click()));
        final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFirst.SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
        onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithoutExtras() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(0, click()));
        final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFirst.SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS;
        onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        final String entryOfSomeListPreference = "Home";
        onView(searchButton()).perform(click());
        // When searching for an entry of a ListPreference
        onView(searchView()).perform(replaceText(entryOfSomeListPreference), closeSoftKeyboard());
        // Then this entry is displayed in the search results
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(entryOfSomeListPreference)));
    }

    @Test
    public void shouldSearchAndNotFindInvisiblePreference() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("invisible"), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(recyclerViewHasItemCount(equalTo(0))));
    }

    @Test
    public void shouldSearchAndFind_ListPreference_showDialog() {
        shouldSearchAndFind_showDialog(
                "this is the dialog title",
                dialogTitle(),
                "this is the dialog title");
    }

    @Test
    public void shouldSearchAndFind_MultiSelectListPreference_showDialog() {
        shouldSearchAndFind_showDialog(
                "dialog title of a multi select list preference",
                dialogTitle(),
                "dialog title of a multi select list preference");
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_showDialog() {
        shouldSearchAndFind_showDialog(
                "some text in a custom dialog",
                customDialogContent(),
                "some text in a custom dialog");
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_PreferenceWithOnPreferenceClickListener_showDialog() {
        shouldSearchAndFind_showDialog(
                "some summary for PreferenceWithOnPreferenceClickListener",
                customDialogContent(),
                "some text in a custom dialog");
    }

    private static void shouldSearchAndFind_showDialog(final String searchQuery,
                                                       final Matcher<View> dialogMatcher,
                                                       final String textWithinDialog) {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(searchQuery), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(0, click()));
        onView(dialogMatcher).check(matches(withText(textWithinDialog)));
    }

    private static Matcher<View> searchButton() {
        return allOf(
                withId(de.KnollFrank.settingssearch.R.id.search_action),
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
                withId(SearchConfigFactory.SEARCH_RESULTS_VIEW_ID),
                isDisplayed());
    }

    private static Matcher<View> dialogTitle() {
        return allOf(
                withId(com.google.android.material.R.id.alertTitle),
                isDisplayed());
    }

    private static Matcher<View> customDialogContent() {
        return allOf(
                withId(R.id.textView),
                withParent(withParent(withId(android.R.id.content))),
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
