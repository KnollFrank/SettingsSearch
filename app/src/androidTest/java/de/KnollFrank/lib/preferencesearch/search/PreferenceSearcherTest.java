package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.fragment.FragmentsFactory;
import de.KnollFrank.lib.preferencesearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceScreensMerger;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceSearcherTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @After
    public void afterClass() {
        // Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldSearchAndFindTitle() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                keyword,
                hasItem(containsString(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindSummary() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                keyword,
                hasItem(containsString(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        final String keyword = "entry of some ListPreference";
        final String keyOfPreference = "keyOfSomeListPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final ListPreference preference = new ListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select from a list");
                            preference.setTitle("Select list preference");
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                keyword,
                hasItem(is(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindMultiSelectListPreference() {
        final String keyword = "entry of some MultiSelectListPreference";
        final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final MultiSelectListPreference preference = new MultiSelectListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select multiple entries from a list");
                            preference.setTitle("Multi select list preference");
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                keyword,
                hasItem(containsString(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndNotFind() {
        final String keyword = "non_existing_keyword";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This checkbox is a preference coming from a fourth file");
                            preference.setTitle("Checkbox fourth file");
                            return preference;
                        }),
                keyword,
                not(hasItem(containsString(keyOfPreference))));
    }

    private static void testSearch(final PreferenceFragmentCompat preferenceFragment,
                                   final String keyword,
                                   final Matcher<Iterable<? super String>> preferenceKeyMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                getPreferences(preferenceFragment, fragmentActivity));

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                assertThat(getKeys(preferenceMatches), preferenceKeyMatcher);
            });
        }
    }

    private static List<Preference> getPreferences(final PreferenceFragmentCompat preferenceFragment,
                                                   final FragmentActivity fragmentActivity) {
        final MergedPreferenceScreen mergedPreferenceScreen = getMergedPreferenceScreen(preferenceFragment, fragmentActivity);
        return Preferences.getAllPreferences(mergedPreferenceScreen.preferenceScreen);
    }

    private static MergedPreferenceScreen getMergedPreferenceScreen(
            final PreferenceFragmentCompat preferenceFragment,
            final FragmentActivity fragmentActivity) {
        final Fragments fragments =
                FragmentsFactory.createFragments(
                        (fragmentClassName, context) ->
                                fragmentClassName.equals(preferenceFragment.getClass().getName()) ?
                                        preferenceFragment :
                                        Fragment.instantiate(context, fragmentClassName),
                        fragmentActivity,
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        fragments,
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments)),
                        new PreferenceScreensMerger(fragmentActivity));
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(preferenceFragment.getClass().getName());
    }

    private static Set<String> getKeys(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .map(Preference::getKey)
                .filter(Objects::nonNull)
                .map(CharSequence::toString)
                .collect(Collectors.toSet());
    }
}