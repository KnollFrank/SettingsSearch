package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
    public void shouldSearchAndFind() {
        final String keyword = "fourth";
        final String title = String.format("Checkbox %s file", keyword);
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
                            checkBoxPreference.setKey("fourthfile");
                            checkBoxPreference.setSummary(
                                    String.format(
                                            "This checkbox is a preference coming from a %s file",
                                            keyword));
                            checkBoxPreference.setTitle(title);
                            return checkBoxPreference;
                        }),
                keyword,
                hasItem(containsString(title)));
    }

    @Test
    public void shouldSearchAndFindListPreference() {
        final String keyword = "entry of some ListPreference";
        final String title = "Select list preference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final ListPreference listPreference = new ListPreference(context);
                            listPreference.setKey("keyOfSomeListPreference");
                            listPreference.setSummary("This allows to select from a list");
                            listPreference.setTitle(title);
                            listPreference.setEntries(new String[]{keyword});
                            return listPreference;
                        }),
                keyword,
                hasItem(containsString(title)));
    }

    @Test
    public void shouldSearchAndFindMultiSelectListPreference() {
        final String keyword = "entry of some MultiSelectListPreference";
        final String title = "Multi select list preference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final MultiSelectListPreference multiSelectListPreference = new MultiSelectListPreference(context);
                            multiSelectListPreference.setKey("keyOfSomeMultiSelectListPreference");
                            multiSelectListPreference.setSummary("This allows to select multiple entries from a list");
                            multiSelectListPreference.setTitle(title);
                            multiSelectListPreference.setEntries(new String[]{keyword});
                            return multiSelectListPreference;
                        }),
                keyword,
                hasItem(containsString(title)));
    }

    @Test
    public void shouldSearchAndNotFind() {
        final String keyword = "non_existing_keyword";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
                            checkBoxPreference.setKey("fourthfile");
                            checkBoxPreference.setSummary("This checkbox is a preference coming from a fourth file");
                            checkBoxPreference.setTitle("Checkbox fourth file");
                            return checkBoxPreference;
                        }),
                keyword,
                not(hasItem(containsString(keyword))));
    }

    private static void testSearch(final PreferenceFragmentCompat preferenceFragment,
                                   final String keyword,
                                   final Matcher<Iterable<? super String>> titleMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                getPreferences(preferenceFragment, fragmentActivity));

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                // FK-TODO: replace titleMatcher with a preferenceKeyMatcher?
                assertThat(getTitles(preferenceMatches), titleMatcher);
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

    private static Set<String> getTitles(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .map(Preference::getTitle)
                .filter(Objects::nonNull)
                .map(CharSequence::toString)
                .collect(Collectors.toSet());
    }
}