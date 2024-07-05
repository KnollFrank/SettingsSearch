package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;
import static de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions.getSearchableInfoProviders;
import static de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions.getSummaryResetterFactories;
import static de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions.getSummarySetters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.fragment.FragmentsFactory;
import de.KnollFrank.lib.preferencesearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SummaryResetterFactories;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetter;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSummaryResetter;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSummarySetter;
import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceSearcherTest {

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
                (preference, host) -> true,
                keyword,
                hasItem(keyOfPreference));
    }

    @Test
    public void shouldSearchAndNotFindNonSearchablePreference() {
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
                (preference, host) -> !keyOfPreference.equals(preference.getKey()),
                keyword,
                not(hasItem(keyOfPreference)));
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
                (preference, host) -> true,
                keyword,
                hasItem(keyOfPreference));
    }

    @Test
    public void shouldSearchAndFindListPreference_entries() {
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
                (preference, host) -> true,
                keyword,
                hasItem(keyOfPreference));
    }

    @Test
    public void shouldSearchAndFindLSwitchPreference_summaryOff() {
        final String summaryOff = "switch is off";
        final String keyOfPreference = "keyOfSomeSwitchPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final SwitchPreference preference = new SwitchPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to use a switch");
                            preference.setSummaryOff(summaryOff);
                            preference.setSummaryOn("switch is on");
                            return preference;
                        }),
                (preference, host) -> true,
                summaryOff,
                hasItem(keyOfPreference));
    }

    @Test
    public void shouldSearchAndFindLSwitchPreference_summaryOn() {
        final String summaryOn = "switch is on";
        final String keyOfPreference = "keyOfSomeSwitchPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final SwitchPreference preference = new SwitchPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to use a switch");
                            preference.setSummaryOff("switch is off");
                            preference.setSummaryOn(summaryOn);
                            return preference;
                        }),
                (preference, host) -> true,
                summaryOn,
                hasItem(keyOfPreference));
    }

    @Test
    public void shouldSearchAndFindCustomReversedListPreferenceViaReversedKeyword() {
        final String keyword = "Windows Live";
        final String keyOfPreference = "keyOfReversedListPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final ReversedListPreference preference = new ReversedListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("summary of ReversedListPreference");
                            preference.setTitle("title of ReversedListPreference");
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                (preference, host) -> true,
                ReversedListPreference.getReverse(keyword).toString(),
                hasItem(keyOfPreference));
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
                (preference, host) -> true,
                keyword,
                hasItem(keyOfPreference));
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
                (preference, host) -> true,
                keyword,
                not(hasItem(keyOfPreference)));
    }

    private static void testSearch(final PreferenceFragmentCompat preferenceFragment,
                                   final SearchablePreferencePredicate searchablePreferencePredicate,
                                   final String keyword,
                                   final Matcher<Iterable<? super String>> preferenceKeyMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final List<PreferenceDescription> preferenceDescriptions =
                        ImmutableList
                                .<PreferenceDescription>builder()
                                .addAll(createBuiltinPreferenceDescriptions())
                                .addAll(
                                        ImmutableList.of(
                                                new PreferenceDescription<>(
                                                        ReversedListPreference.class,
                                                        new ReversedListPreferenceSearchableInfoProvider(),
                                                        new ReversedListPreferenceSummarySetter(),
                                                        ReversedListPreferenceSummaryResetter::new)))
                                .build();

                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                getMergedPreferenceScreen(
                                        preferenceFragment,
                                        searchablePreferencePredicate,
                                        getSummaryResetterFactories(preferenceDescriptions),
                                        fragmentActivity),
                                new SummarySetter(getSummarySetters(preferenceDescriptions)),
                                new SearchableInfoProviderInternal(getSearchableInfoProviders(preferenceDescriptions)));

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                assertThat(getKeys(preferenceMatches), preferenceKeyMatcher);
            });
        }
    }

    private static MergedPreferenceScreen getMergedPreferenceScreen(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchablePreferencePredicate searchablePreferencePredicate,
            final SummaryResetterFactories summaryResetterFactories,
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
                        new PreferenceScreensMerger(fragmentActivity),
                        searchablePreferencePredicate,
                        summaryResetterFactories,
                        false);
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(preferenceFragment.getClass().getName());
    }

    private static Set<String> getKeys(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .map(Preference::getKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}