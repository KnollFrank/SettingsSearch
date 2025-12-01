package de.KnollFrank.settingssearch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
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
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_TITLE;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.SUMMARY_CHANGING_PREFERENCE_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig;
import de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.test.LocalePreferenceSearchExample;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PreferenceSearchExampleTest {

    @Before
    public void setUp() {
        LocalePreferenceSearchExample.unsetLocale();
    }

    @After
    public void tearDown() {
        LocalePreferenceSearchExample.unsetLocale();
    }

    @Test
    public void shouldSearchAndFindPreference() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText("fourth"), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring("Checkbox fourth file")));
        }
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithExtras() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            searchForQueryThenClickSearchResultAtPosition(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS, 0);
            final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFifth.SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
            onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
        }
    }

    @Test
    public void shouldFullyInstantiatePreferenceFragmentOfClickedSearchResult_srcIsPreferenceWithoutExtras() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            searchForQueryThenClickSearchResultAtPosition(PreferenceFragmentWithSinglePreference.TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS, 0);
            final String summaryOfPreferenceOfFullyInstantiatedPreferenceFragment = "copied summary: " + PrefsFragmentFifth.SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS;
            onView(summaryOfPreference()).check(matches(withText(summaryOfPreferenceOfFullyInstantiatedPreferenceFragment)));
        }
    }

    // fails
    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivity() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Your signature";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(dialogTitle()).check(matches(withText(query)));
        }
    }

    // fails
    @Test
    public void shouldSearchAndFindPreferenceFromTwoActivitiesApart() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Your signature2";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(dialogTitle()).check(matches(withText(query)));
        }
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivityInFragment1() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Item 1";
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(query)));
        }
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivityInFragment2() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Item3 1";
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(query)));
        }
    }

    @Test
    public void shouldSearchAndFindPreferenceReferencingAnotherActivity() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Preference with Intent";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(titleOfPreference(query)).check(matches(withText(query)));
        }
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String entryOfSomeListPreference = getEmailAddressTypes()[0];
            onView(searchButton()).perform(click());
            // When searching for an entry of a ListPreference
            onView(searchView()).perform(replaceText(entryOfSomeListPreference), closeSoftKeyboard());
            // Then this entry is displayed in the search results
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(entryOfSomeListPreference)));
        }
    }

    @Test
    public void shouldSearchAndNotFindInvisiblePreference() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText("invisible"), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(recyclerViewHasItemCount(equalTo(0))));
        }
    }

    @Test
    public void shouldSearchAndFind_ListPreference_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "this is the dialog title";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(dialogTitle()).check(matches(withText(query)));
        }
    }

    @Test
    public void shouldSearchAndFind_MultiSelectListPreference_queryInSearchableInfo_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String queryInSearchableInfo = "dialog title of a multi select list preference";
            searchForQueryThenClickSearchResultAtPosition(queryInSearchableInfo, 0);
            onView(dialogTitle()).check(matches(withText(queryInSearchableInfo)));
        }
    }

    @Test
    public void shouldSearchAndFind_MultiSelectListPreference_queryNotInSearchableInfo_doNotShowDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String queryNotInSearchableInfo = "This allows to select multiple entries from a list";
            searchForQueryThenClickSearchResultAtPosition(queryNotInSearchableInfo, 0);
            onView(dialogTitle()).check(doesNotExist());
        }
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "some text in a custom dialog";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(customDialogContent()).check(matches(withText(query)));
        }
    }

    @Test
    public void shouldSearchAndFind_DropDownPreference_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "Windows Live";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(dialogTitle()).check(matches(withText("im Protocols title")));
        }
    }

    @Test
    public void shouldSearchAndFind_CustomDialogPreference_PreferenceWithOnPreferenceClickListener_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            searchForQueryThenClickSearchResultAtPosition("some text in a custom dialog", 2);
            onView(customDialogContent()).check(matches(withText("some text in a custom dialog")));
        }
    }

    @Test
    public void shouldSearchAndFind_ReversedListPreference_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            searchForQueryThenClickSearchResultAtPosition("swodniW", 0);
            onView(dialogTitle()).check(matches(withText("title of ReversedListPreference")));
        }
    }

    @Test
    public void shouldSearchAndFind_EditTextPreference_showDialog() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final String query = "this is the edit text dialog title";
            searchForQueryThenClickSearchResultAtPosition(query, 0);
            onView(dialogTitle()).check(matches(withText(query)));
        }
    }

    @Test
    public void shouldSearchAndNotFindNonAddedPreference() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            preferencesContainer().perform(actionOnItem(someTitleToFifthFragment(), click()));
            uncheckCheckBoxExplicitly(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, actionOnItem(addPreferenceToP1(), click()));
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(SOME_ADDITIONAL_PREFERENCE), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(recyclerViewHasItemCount(equalTo(0))));
        }
    }

    @Test
    public void shouldSearchAndFindAddedPreference() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            preferencesContainer().perform(actionOnItem(someTitleToFifthFragment(), click()));
            checkCheckBoxExplicitly(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, actionOnItem(addPreferenceToP1(), click()));
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(SOME_ADDITIONAL_PREFERENCE), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(SOME_ADDITIONAL_PREFERENCE)));
        }
    }

    @Test
    public void shouldSearchAndNotFindNonAddedPreference_usingPrepackagedDatabaseAssetFile() {
        test_searchAndFindAddedPreference_usingPrepackagedDatabaseAssetFile(false);
    }

    @Test
    public void shouldSearchAndFindAddedPreference_usingPrepackagedDatabaseAssetFile() {
        test_searchAndFindAddedPreference_usingPrepackagedDatabaseAssetFile(true);
    }

    @Test
    public void shouldSearchAndFindSummaryChangingPreferenceIsON() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final boolean checked = true;
            checkCheckBoxExplicitly(SUMMARY_CHANGING_PREFERENCE_KEY, actionOnItem(summaryChangingPreference(), click()));
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(PrefsFragmentFirst.getSummary(checked)), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(PrefsFragmentFirst.getSummary(checked))));
        }
    }

    @Test
    public void shouldSearchAndFindSummaryChangingPreferenceIsOFF() {
        try (final ActivityScenario<PreferenceSearchExample> scenario = ActivityScenario.launch(PreferenceSearchExample.class)) {
            final boolean checked = false;
            uncheckCheckBoxExplicitly(SUMMARY_CHANGING_PREFERENCE_KEY, actionOnItem(summaryChangingPreference(), click()));
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(PrefsFragmentFirst.getSummary(checked)), closeSoftKeyboard());
            onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(PrefsFragmentFirst.getSummary(checked))));
        }
    }

    private static void searchForQueryThenClickSearchResultAtPosition(final String query, final int position) {
        onView(searchButton()).perform(click());
        onView(searchView()).perform(replaceText(query), closeSoftKeyboard());
        onView(searchResultsView()).perform(actionOnItemAtPosition(position, click()));
    }

    public static Matcher<View> searchButton() {
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

    public static Matcher<View> searchView() {
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

    private void checkCheckBoxExplicitly(final String key, final ViewAction clickCheckBox) {
        createCheckBoxHandler(key, clickCheckBox).checkCheckBoxExplicitly();
    }

    private void uncheckCheckBoxExplicitly(final String key, final ViewAction clickCheckBox) {
        createCheckBoxHandler(key, clickCheckBox).uncheckCheckBoxExplicitly();
    }

    private static CheckBoxHandler createCheckBoxHandler(final String key, final ViewAction clickCheckBox) {
        return CheckBoxHandler.of(key, preferencesContainer(), clickCheckBox);
    }

    private static ViewInteraction preferencesContainer() {
        return onView(
                allOf(
                        withId(androidx.preference.R.id.recycler_view),
                        childAtPosition(
                                withId(android.R.id.list_container),
                                0)));
    }

    private static String[] getEmailAddressTypes() {
        return getTargetContext()
                .getResources()
                .getStringArray(android.R.array.emailAddressTypes);
    }

    private static Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    private static void setupToEnsureCreateFromPrepackagedDatabaseAssetFile() {
        DatabaseFileDeleter.deleteDatabaseFile(
                getTargetContext(),
                PreferencesDatabaseFactory.SEARCHABLE_PREFERENCES_DB);
    }

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
    }

    private static void test_searchAndFindAddedPreference_usingPrepackagedDatabaseAssetFile(final boolean shallFindAdditionalPreference) {
        setupToEnsureCreateFromPrepackagedDatabaseAssetFile();
        PreferenceSearchExampleTest
                .getSharedPreferences()
                .edit()
                .putBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, shallFindAdditionalPreference)
                .commit();
        try (final ActivityScenario<LocalePreferenceSearchExample> scenario = ActivityScenario.launch(LocalePreferenceSearchExample.class)) {
            scenario.onActivity(activity -> LocalePreferenceSearchExample.setLocale(getSomeLocaleFromPrepackagedDatabase(activity)));
            onView(searchButton()).perform(click());
            onView(searchView()).perform(replaceText(SOME_ADDITIONAL_PREFERENCE), closeSoftKeyboard());
            if (shallFindAdditionalPreference) {
                onView(searchResultsView()).check(matches(hasSearchResultWithSubstring(SOME_ADDITIONAL_PREFERENCE)));
            } else {
                onView(searchResultsView()).check(matches(recyclerViewHasItemCount(equalTo(0))));
            }
        }
    }

    private static Locale getSomeLocaleFromPrepackagedDatabase(final FragmentActivity activityContext) {
        final PreferencesDatabaseConfig<Configuration> preferencesDatabaseConfig = PreferencesDatabaseFactory.createPreferencesDatabaseConfigUsingPrepackagedDatabaseAssetFile();
        final PreferencesDatabase<Configuration> preferencesDatabase =
                getPreferencesDatabase(
                        preferencesDatabaseConfig.databaseFileName(),
                        preferencesDatabaseConfig
                                .prepackagedPreferencesDatabase()
                                .orElseThrow()
                                .databaseAssetFile());
        final Locale locale =
                preferencesDatabase
                        .searchablePreferenceScreenGraphRepository()
                        .loadAll(null, activityContext)
                        .stream()
                        .findAny()
                        .orElseThrow()
                        .locale();
        preferencesDatabase.close();
        return locale;
    }

    private static PreferencesDatabase<Configuration> getPreferencesDatabase(final String databaseFileName,
                                                                             final File databasedAssetFile) {
        return Room
                .databaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        PreferencesDatabase.class,
                        databaseFileName)
                .createFromAsset(databasedAssetFile.getPath())
                .allowMainThreadQueries()
                .build();
    }

    private static Matcher<View> someTitleToFifthFragment() {
        return hasDescendant(hasTitle("some title to fifth fragment"));
    }

    private static Matcher<View> addPreferenceToP1() {
        return hasDescendant(hasTitle(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_TITLE));
    }

    private static Matcher<View> summaryChangingPreference() {
        return hasDescendant(hasTitle("changes summary when clicked"));
    }

    private static Matcher<View> hasTitle(final String title) {
        return allOf(
                withId(android.R.id.title),
                withText(title));
    }
}
