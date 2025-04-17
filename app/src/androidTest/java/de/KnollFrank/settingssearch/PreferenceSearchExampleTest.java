package de.KnollFrank.settingssearch;

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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.settingssearch.Matchers.childAtPosition;
import static de.KnollFrank.settingssearch.Matchers.recyclerViewHasItem;
import static de.KnollFrank.settingssearch.Matchers.recyclerViewHasItemCount;
import static de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference.SOME_ADDITIONAL_PREFERENCE;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY;

import android.content.SharedPreferences;
import android.view.View;

import androidx.preference.PreferenceManager;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PreferenceSearchExampleTest {

    @Rule
    public ActivityScenarioRule<PreferenceSearchExample> activityScenarioRule = new ActivityScenarioRule<>(PreferenceSearchExample.class);

    @Test
    public void shouldSearchAndFindPreference() {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText("fourth"), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring("Checkbox fourth file")));
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithExtras() {
        searchForQueryThenClickSearchResultAtPosition(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS, 0);
        final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFirst.SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
        onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithoutExtras() {
        searchForQueryThenClickSearchResultAtPosition(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS, 0);
        final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFirst.SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS;
        onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivity() {
        final String query = "Your signature";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(titleOfPreference(query)).check(matches(withText(query)));
    }

    @Test
    public void shouldSearchAndFindPreferenceFromTwoActivitiesApart() {
        final String query = "Your signature2";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(titleOfPreference(query)).check(matches(withText(query)));
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivityInFragment1() {
        final String query = "Item 1";
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(query)));
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivityInFragment2() {
        final String query = "Item3 1";
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(query)));
    }

    @Test
    public void shouldSearchAndFindPreferenceReferencingAnotherActivity() {
        final String query = "Preference with Intent";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(titleOfPreference(query)).check(matches(withText(query)));
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
        final String query = "this is the dialog title";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(dialogTitle()).check(matches(withText(query)));
    }

    @Test
    public void shouldSearchAndFind_MultiSelectListPreference_queryInSearchableInfo_showDialog() {
        final String queryInSearchableInfo = "dialog title of a multi select list preference";
        searchForQueryThenClickSearchResultAtPosition(queryInSearchableInfo, 0);
        onView(dialogTitle()).check(matches(withText(queryInSearchableInfo)));
    }

    @Test
    public void shouldSearchAndFind_MultiSelectListPreference_queryNotInSearchableInfo_doNotShowDialog() {
        final String queryNotInSearchableInfo = "This allows to select multiple entries from a list";
        searchForQueryThenClickSearchResultAtPosition(queryNotInSearchableInfo, 0);
        onView(dialogTitle()).check(doesNotExist());
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_showDialog() {
        final String query = "some text in a custom dialog";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(customDialogContent()).check(matches(withText(query)));
    }

    @Test
    public void shouldSearchAndFind_DropDownPreference_showDialog() {
        final String query = "Windows Live";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(dialogTitle()).check(matches(withText("im Protocols title")));
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_PreferenceWithOnPreferenceClickListener_showDialog() {
        searchForQueryThenClickSearchResultAtPosition("some text in a custom dialog", 2);
        onView(customDialogContent()).check(matches(withText("some text in a custom dialog")));
    }

    @Test
    public void shouldSearchAndFind_ReversedListPreference_showDialog() {
        searchForQueryThenClickSearchResultAtPosition("swodniW", 0);
        onView(dialogTitle()).check(matches(withText("title of ReversedListPreference")));
    }

    @Test
    public void shouldSearchAndFind_EditTextPreference_showDialog() {
        final String query = "this is the edit text dialog title";
        searchForQueryThenClickSearchResultAtPosition(query, 0);
        onView(dialogTitle()).check(matches(withText(query)));
    }

    @Test
    public void shouldSearchAndNotFindNonAddedPreference() {
        uncheckAddPreferenceToP1CheckBoxExplicitly();
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(SOME_ADDITIONAL_PREFERENCE), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(recyclerViewHasItemCount(equalTo(0))));
    }

    @Test
    public void shouldSearchAndFindAddedPreference() {
        checkAddPreferenceToP1CheckBoxExplicitly();
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(SOME_ADDITIONAL_PREFERENCE), closeSoftKeyboard());
        onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(SOME_ADDITIONAL_PREFERENCE)));
    }

    private static ViewInteraction preferencesContainer() {
        return onView(
                allOf(
                        withId(androidx.preference.R.id.recycler_view),
                        childAtPosition(
                                withId(android.R.id.list_container),
                                0)));
    }

    private static void searchForQueryThenClickSearchResultAtPosition(final String query, final int position) {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(position, click()));
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

    private static Matcher<View> titleOfPreference(final String title) {
        return allOf(
                withId(android.R.id.title),
                withText(title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class))),
                isDisplayed());
    }

    private static Matcher<View> summaryOfPreference() {
        return allOf(
                withId(android.R.id.summary),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class))),
                isDisplayed());
    }

    private void checkAddPreferenceToP1CheckBoxExplicitly() {
        uncheckAddPreferenceToP1CheckBox();
        checkAddPreferenceToP1CheckBox();
    }

    private void uncheckAddPreferenceToP1CheckBoxExplicitly() {
        checkAddPreferenceToP1CheckBox();
        uncheckAddPreferenceToP1CheckBox();
    }

    private void checkAddPreferenceToP1CheckBox() {
        if (!isAddPreferenceToP1CheckBoxChecked()) {
            clickAddPreferenceToP1CheckBox();
        }
    }

    private void uncheckAddPreferenceToP1CheckBox() {
        if (isAddPreferenceToP1CheckBoxChecked()) {
            clickAddPreferenceToP1CheckBox();
        }
    }

    private boolean isAddPreferenceToP1CheckBoxChecked() {
        return getSharedPreferences().getBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, false);
    }

    private SharedPreferences getSharedPreferences() {
        final SharedPreferences[] sharedPreferencesHolder = new SharedPreferences[1];
        activityScenarioRule.getScenario().onActivity(activity -> sharedPreferencesHolder[0] = PreferenceManager.getDefaultSharedPreferences(activity));
        return Objects.requireNonNull(sharedPreferencesHolder[0]);
    }

    private static void clickAddPreferenceToP1CheckBox() {
        final int positionOfAddPreferenceToP1CheckBox = 26;
        preferencesContainer().perform(actionOnItemAtPosition(positionOfAddPreferenceToP1CheckBox, click()));
    }
}
