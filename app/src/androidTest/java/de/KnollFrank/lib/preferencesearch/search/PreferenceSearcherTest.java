package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.FragmentsFactory;
import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceScreensMerger;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceSearcherTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldSearchAndFind() {
        final String keyword = PrefsFragment.SEARCH_QUERY_FOR_SOME_PREFERENCE;
        testSearch(PrefsFragment.class, keyword, hasItem(containsString(keyword)));
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        final String keyword = PrefsFragment.SEARCH_QUERY_FOR_SOME_ENTRY_OF_A_LIST_PREFERENCE;
        testSearch(PrefsFragment.class, keyword, hasItem(containsString("List preference")));
    }

    @Test
    public void shouldSearchAndNotFind() {
        final String keyword = PrefsFragment.SEARCH_QUERY_FOR_SOME_NON_EXISTING_PREFERENCE;
        testSearch(PrefsFragment.class, keyword, not(hasItem(containsString(keyword))));
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        public static final String SEARCH_QUERY_FOR_SOME_PREFERENCE = "fourth";
        public static final String SEARCH_QUERY_FOR_SOME_NON_EXISTING_PREFERENCE = "non_existing_keyword";
        public static final String SEARCH_QUERY_FOR_SOME_ENTRY_OF_A_LIST_PREFERENCE = "entry of some Listpreference";

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.addPreference(createPreference(context));
            screen.addPreference(createListPreference(context));
            setPreferenceScreen(screen);
        }

        // FK-TODO: allow to create a Preference within test methods via a callback of type Context -> Preference
        private static Preference createPreference(final Context context) {
            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setKey("fourthfile");
            checkBoxPreference.setSummary(String.format("This checkbox is a preference coming from a %s file", SEARCH_QUERY_FOR_SOME_PREFERENCE));
            checkBoxPreference.setTitle(String.format("Checkbox %s file", SEARCH_QUERY_FOR_SOME_PREFERENCE));
            return checkBoxPreference;
        }

        private static Preference createListPreference(final Context context) {
            final ListPreference listPreference = new ListPreference(context);
            listPreference.setKey("keyOfSomeListPreference");
            listPreference.setSummary("This allows to select from a list");
            listPreference.setTitle("List preference");
            listPreference.setEntries(new String[]{SEARCH_QUERY_FOR_SOME_ENTRY_OF_A_LIST_PREFERENCE});
            return listPreference;
        }
    }

    private static void testSearch(final Class<? extends PreferenceFragmentCompat> preferenceScreen,
                                   final String keyword,
                                   final Matcher<Iterable<? super String>> titlesMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                getPreferences(preferenceScreen, fragmentActivity));

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                // FK-TODO: replace titlesMatcher with a preferenceKeyMatcher?
                assertThat(getTitles(preferenceMatches), titlesMatcher);
            });
        }
    }

    private static List<Preference> getPreferences(final Class<? extends PreferenceFragmentCompat> preferenceScreen,
                                                   final TestActivity fragmentActivity) {
        final MergedPreferenceScreen mergedPreferenceScreen = getMergedPreferenceScreen(preferenceScreen, fragmentActivity);
        return Preferences.getAllPreferences(mergedPreferenceScreen.preferenceScreen);
    }

    private static MergedPreferenceScreen getMergedPreferenceScreen(
            final Class<? extends PreferenceFragmentCompat> preferenceScreen,
            final TestActivity fragmentActivity) {
        final Fragments fragments = FragmentsFactory.createFragments(fragmentActivity);
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        fragments,
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments)),
                        new PreferenceScreensMerger(fragmentActivity));
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(preferenceScreen.getName());
    }

    private static List<String> getTitles(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .map(Preference::getTitle)
                .filter(Objects::nonNull)
                .map(CharSequence::toString)
                .collect(Collectors.toList());
    }
}