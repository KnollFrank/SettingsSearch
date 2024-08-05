package de.KnollFrank.lib.preferencesearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.preferencesearch.search.provider.BuiltinPreferenceDescriptionsFactory.createBuiltinPreferenceDescriptions;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentFactoryAndInitializer;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.preferencesearch.fragment.FragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceDialogProvider;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceScreensMerger;
import de.KnollFrank.lib.preferencesearch.provider.SearchablePreferencePredicate;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescription;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescriptions;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProviders;
import de.KnollFrank.preferencesearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.preferencesearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.preferencesearch.preference.fragment.PrefsFragmentFirst;
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                not(hasItem(keyOfPreference))));
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindListPreference_dialogTitle() {
        final String keyword = "this is the dialog title";
        final String keyOfPreference = "keyOfSomeListPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final ListPreference preference = new ListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select from a list");
                            preference.setTitle("Select list preference");
                            preference.setEntries(new String[]{"some entry"});
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setDialogTitle(keyword);
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindSwitchPreference_summaryOff() {
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindSwitchPreference_summaryOn() {
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                (preference, host) -> true,
                ReversedListPreference.getReverse(keyword).toString(),
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindInCustomDialogPreference() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = "keyOfCustomDialogPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CustomDialogPreference preference = new CustomDialogPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("summary of CustomDialogPreference");
                            preference.setTitle("title of CustomDialogPreference");
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) ->
                        preference instanceof CustomDialogPreference ?
                                Optional.of(new CustomDialogFragment()) :
                                Optional.empty(),
                preferenceDialog -> {
                    if (preferenceDialog instanceof final CustomDialogFragment customDialogFragment) {
                        return customDialogFragment.getSearchableInfo();
                    }
                    throw new IllegalArgumentException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindInPreferenceWithOnPreferenceClickListener() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = "keyOfPreferenceWithOnPreferenceClickListener";
        testSearch(
                new PrefsFragmentFirst(),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) ->
                        keyOfPreference.equals(preference.getKey()) ?
                                Optional.of(new CustomDialogFragment()) :
                                Optional.empty(),
                preferenceDialog -> {
                    if (preferenceDialog instanceof final CustomDialogFragment customDialogFragment) {
                        return customDialogFragment.getSearchableInfo();
                    }
                    throw new IllegalArgumentException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                            preference.setEntryValues(new String[]{"some entry value"});
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindMultiSelectListPreference_dialogTitle() {
        final String keyword = "dialog title of some MultiSelectListPreference";
        final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final MultiSelectListPreference preference = new MultiSelectListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select multiple entries from a list");
                            preference.setTitle("Multi select list preference");
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setEntries(new String[]{"some entry"});
                            preference.setDialogTitle(keyword);
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                not(hasItem(keyOfPreference))));
    }

    @Test
    public void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
        final String keyword = "some preference of connected fragment";
        final String keyOfPreference = "keyOfPreferenceOfConnectedFragment";
        final PreferenceFragment connectedFragment =
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final Preference preference = new Preference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(keyword);
                            return preference;
                        });
        final FragmentWithConnection fragmentWithConnection =
                FragmentWithConnection.createFragmentConnectedTo(connectedFragment.getClass());
        testSearch(
                fragmentWithConnection,
                (preference, host) -> true,
                keyword,
                (hostOfPreference, preference) -> Optional.empty(),
                preferenceDialog -> {
                    throw new IllegalStateException();
                },
                createFragmentFactoryReturning(fragmentWithConnection, connectedFragment),
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                contains(keyOfPreference, keyOfPreference)));
    }

    public static class FragmentWithConnection extends PreferenceFragmentCompat {

        private final Class<? extends PreferenceFragmentCompat> connectedFragment;

        public static FragmentWithConnection createFragmentConnectedTo(final Class<? extends PreferenceFragmentCompat> connectedFragment) {
            return new FragmentWithConnection(connectedFragment);
        }

        public FragmentWithConnection(final Class<? extends PreferenceFragmentCompat> connectedFragment) {
            this.connectedFragment = connectedFragment;
        }

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("screen with connection");
            {
                final Preference preference = createPreferenceConnectedTo(connectedFragment, context);
                preference.setTitle("first preference connected to " + connectedFragment.getSimpleName());
                screen.addPreference(preference);
            }
            {
                final Preference preference = createPreferenceConnectedTo(connectedFragment, context);
                preference.setTitle("second preference connected to " + connectedFragment.getSimpleName());
                screen.addPreference(preference);
            }
            setPreferenceScreen(screen);
        }

        private static Preference createPreferenceConnectedTo(final Class<? extends Fragment> connectedFragment,
                                                              final Context context) {
            final Preference preference = new Preference(context);
            preference.setFragment(connectedFragment.getName());
            return preference;
        }
    }

    private static void testSearch(final PreferenceFragmentCompat preferenceFragment,
                                   final SearchablePreferencePredicate searchablePreferencePredicate,
                                   final String keyword,
                                   final PreferenceDialogProvider preferenceDialogProvider,
                                   final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider,
                                   final Consumer<List<PreferenceMatch>> checkPreferenceMatches) {
        testSearch(
                preferenceFragment,
                searchablePreferencePredicate,
                keyword,
                preferenceDialogProvider,
                searchableInfoByPreferenceDialogProvider,
                createFragmentFactoryReturning(preferenceFragment),
                checkPreferenceMatches);
    }

    private static FragmentFactory createFragmentFactoryReturning(final PreferenceFragmentCompat... preferenceFragments) {
        return (fragmentClassName, src, context) -> {
            for (final PreferenceFragmentCompat preferenceFragment : preferenceFragments) {
                if (preferenceFragment.getClass().getName().equals(fragmentClassName)) {
                    return preferenceFragment;
                }
            }
            return Fragment.instantiate(context, fragmentClassName);
        };
    }

    private static void testSearch(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchablePreferencePredicate searchablePreferencePredicate,
            final String keyword,
            final PreferenceDialogProvider preferenceDialogProvider,
            final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider,
            final FragmentFactory fragmentFactory,
            final Consumer<List<PreferenceMatch>> checkPreferenceMatches) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final MergedPreferenceScreen mergedPreferenceScreen =
                        getMergedPreferenceScreen(
                                preferenceFragment,
                                searchablePreferencePredicate,
                                fragmentActivity,
                                preferenceDialogProvider,
                                searchableInfoByPreferenceDialogProvider,
                                fragmentFactory);
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                mergedPreferenceScreen,
                                new SearchableInfoAttribute(),
                                getSearchableInfoProviderInternal(
                                        mergedPreferenceScreen,
                                        ImmutableList
                                                .<PreferenceDescription>builder()
                                                .addAll(createBuiltinPreferenceDescriptions())
                                                .add(new PreferenceDescription<>(
                                                        ReversedListPreference.class,
                                                        new ReversedListPreferenceSearchableInfoProvider()))
                                                .build()));

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                checkPreferenceMatches.accept(preferenceMatches);
            });
        }
    }

    private static MergedPreferenceScreen getMergedPreferenceScreen(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchablePreferencePredicate searchablePreferencePredicate,
            final FragmentActivity fragmentActivity,
            final PreferenceDialogProvider preferenceDialogProvider,
            final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider,
            final FragmentFactory fragmentFactory) {
        final DefaultFragmentInitializer defaultFragmentInitializer =
                new DefaultFragmentInitializer(
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        final Fragments fragments =
                new Fragments(
                        new DefaultFragmentFactoryAndInitializer(
                                fragmentFactory,
                                defaultFragmentInitializer),
                        fragmentActivity);
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        fragments,
                        defaultFragmentInitializer,
                        new PreferenceScreensProvider(new PreferenceScreenWithHostProvider(fragments)),
                        new PreferenceScreensMerger(fragmentActivity),
                        searchablePreferencePredicate,
                        new SearchableInfoAttribute(),
                        preferenceDialogProvider,
                        searchableInfoByPreferenceDialogProvider,
                        false);
        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(preferenceFragment.getClass().getName());
    }

    private static SearchableInfoProviderInternal getSearchableInfoProviderInternal(
            final MergedPreferenceScreen mergedPreferenceScreen,
            final List<PreferenceDescription> preferenceDescriptions) {
        return new SearchableInfoProviderInternal(
                new SearchableInfoProviders(
                        Maps.merge(
                                ImmutableList.of(
                                        PreferenceDescriptions.getSearchableInfoProvidersByPreferenceClass(preferenceDescriptions),
                                        PreferenceDescriptions.getSearchableInfoProvidersByPreferenceClass(mergedPreferenceScreen.getPreferenceDescriptions())),
                                SearchableInfoProvider::mergeWith)));
    }

    private static List<String> getKeys(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .map(Preference::getKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}