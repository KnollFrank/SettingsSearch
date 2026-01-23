package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.settingssearch.graph.TreeBuilderListeners.emptyTreeBuilderListener;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeySet;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableBiMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.DefaultPreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerRegistry;
import de.KnollFrank.lib.settingssearch.graph.PreferenceScreenTreeBuilderFactory;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenTreeProvider;
import de.KnollFrank.lib.settingssearch.graph.TreeBuilderListener;
import de.KnollFrank.lib.settingssearch.graph.TreeToPojoTreeTransformer;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.results.DefaultMarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsByPreferencePathSorter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.recyclerview.DefaultPreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;
import de.KnollFrank.settingssearch.SettingsActivity;
import de.KnollFrank.settingssearch.SettingsActivity.SettingsFragment;
import de.KnollFrank.settingssearch.SettingsActivity2;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class PreferenceSearcherTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldSearchAndFindTitle() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindPreference_includePreferenceInSearchResults() {
        final String keyword = "fourth";
        final String keyOfPreferenceToIncludeInSearchResults = "fourthfile";
        final PreferenceFragmentCompat preferenceFragment =
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreferenceToIncludeInSearchResults);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        });
        testSearch(
                preferenceFragment,
                (preference, hostOfPreference) -> true,
                new SearchResultsFilter() {

                    @Override
                    public boolean includePreferenceInSearchResults(final SearchablePreferenceOfHostWithinTree preference) {
                        return keyOfPreferenceToIncludeInSearchResults.equals(preference.searchablePreference().getKey()) &&
                                preferenceFragment.getClass().equals(preference.hostOfPreference().host());
                    }
                },
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreferenceToIncludeInSearchResults)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindPreference_excludePreferenceFromSearchResults() {
        final String keyword = "fourth";
        final String keyOfPreferenceToExcludeFromSearchResults = "fourthfile";
        final PreferenceFragmentCompat preferenceFragment =
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreferenceToExcludeFromSearchResults);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        });
        testSearch(
                preferenceFragment,
                (preference, hostOfPreference) -> true,
                new SearchResultsFilter() {

                    @Override
                    public boolean includePreferenceInSearchResults(final SearchablePreferenceOfHostWithinTree preference) {
                        return !(keyOfPreferenceToExcludeFromSearchResults.equals(preference.searchablePreference().getKey()) && preferenceFragment.getClass().equals(preference.hostOfPreference().host()));
                    }
                },
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreferenceToExcludeFromSearchResults))),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindTitleOfPreferenceCategory() {
        final String keyword = "This is a preference category";
        final String keyOfPreference = "keyOfPreferenceCategory";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final PreferenceCategory preference = new PreferenceCategory(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format(keyword));
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindNestedPreference() {
        final String keyword = "This is some keyword";
        final String keyOfNestedPreference = "keyOfNestedPreference";
        testSearch(
                new PreferenceFragmentTemplate(
                        (preferenceScreen, context) -> {
                            final PreferenceCategory category = new PreferenceCategory(context);
                            category.setKey("keyOfCategory");
                            preferenceScreen.addPreference(category);

                            final CheckBoxPreference nestedPreference = new CheckBoxPreference(context);
                            nestedPreference.setKey(keyOfNestedPreference);
                            nestedPreference.setTitle(keyword);

                            category.addPreference(nestedPreference);
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfNestedPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndNotFindNonSearchablePreference() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                (preference, hostOfPreference) -> !(preference instanceof CheckBoxPreference && keyOfPreference.equals(preference.getKey())),
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreference))),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindSummary() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindListPreference_entries() {
        final String keyword = "entry of some ListPreference";
        final String keyOfPreference = "keyOfSomeListPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final ListPreference preference = new ListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select from a list");
                            preference.setTitle("Select list preference");
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindListPreference_dialogTitle() {
        final String keyword = "this is the dialog title";
        final String keyOfPreference = "keyOfSomeListPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
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
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindSwitchPreference_summaryOff() {
        final String summaryOff = "switch is off";
        final String keyOfPreference = "keyOfSomeSwitchPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final SwitchPreference preference = new SwitchPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to use a switch");
                            preference.setSummaryOff(summaryOff);
                            preference.setSummaryOn("switch is on");
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                summaryOff,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindSwitchPreference_summaryOn() {
        final String summaryOn = "switch is on";
        final String keyOfPreference = "keyOfSomeSwitchPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final SwitchPreference preference = new SwitchPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to use a switch");
                            preference.setSummaryOff("switch is off");
                            preference.setSummaryOn(summaryOn);
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                summaryOn,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindCustomReversedListPreferenceViaReversedKeyword() {
        final String keyword = "Windows Live";
        final String keyOfPreference = "keyOfReversedListPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final ReversedListPreference preference = new ReversedListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("summary of ReversedListPreference");
                            preference.setTitle("title of ReversedListPreference");
                            preference.setEntryValues(new String[]{"some entry value"});
                            preference.setEntries(new String[]{keyword});
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                ReversedListPreference.getReverse(keyword).toString(),
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindInCustomDialogPreference() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = "keyOfCustomDialogPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CustomDialogPreference preference = new CustomDialogPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("summary of CustomDialogPreference");
                            preference.setTitle("title of CustomDialogPreference");
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindInTwoCustomDialogPreferences() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference1 = "keyOfCustomDialogPreference1";
        final String keyOfPreference2 = "keyOfCustomDialogPreference2";
        testSearch(
                new PreferenceFragmentTemplate(
                        new Function<>() {

                            @Override
                            public List<Preference> apply(final Context context) {
                                return List.of(
                                        createCustomDialogPreference(context, keyOfPreference1),
                                        createCustomDialogPreference(context, keyOfPreference2));
                            }

                            private CustomDialogPreference createCustomDialogPreference(final Context context, final String keyOfPreference) {
                                final CustomDialogPreference preference = new CustomDialogPreference(context);
                                preference.setKey(keyOfPreference);
                                preference.setSummary("summary of CustomDialogPreference");
                                preference.setTitle("title of CustomDialogPreference");
                                return preference;
                            }
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItems(keyOfPreference1, keyOfPreference2)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindInPreferenceWithOnPreferenceClickListener() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER;
        testSearch(
                new PrefsFragmentFirst(),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindPreferenceFromAnotherActivity() {
        final String keyword = "Your signature";
        final String keyOfPreferenceFromSettingsActivity = "signature";
        testSearch(
                new PrefsFragmentFirst(),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreferenceFromSettingsActivity)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindPreferenceFromTwoActivitiesApart() {
        final String keyword = "Your signature2";
        final String keyOfPreferenceFromSettingsActivity = "signature2";
        testSearch(
                new PrefsFragmentFirst(),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreferenceFromSettingsActivity)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindMultiSelectListPreference() {
        final String keyword = "entry of some MultiSelectListPreference";
        final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final MultiSelectListPreference preference = new MultiSelectListPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This allows to select multiple entries from a list");
                            preference.setTitle("Multi select list preference");
                            preference.setEntries(new String[]{keyword});
                            preference.setEntryValues(new String[]{"some entry value"});
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindMultiSelectListPreference_dialogTitle() {
        final String keyword = "dialog title of some MultiSelectListPreference";
        final String keyOfPreference = "keyOfSomeMultiSelectListPreference";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
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
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndNotFind() {
        final String keyword = "non_existing_keyword";
        final String keyOfPreference = "fourthfile";
        testSearch(
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setSummary("This checkbox is a preference coming from a fourth file");
                            preference.setTitle("Checkbox fourth file");
                            return preference;
                        }),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreference))),
                createTreeRepository(),
                emptyTreeBuilderListener());
    }

    @Test
    public void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
        PreferenceSearcherTestCaseTwoDifferentPreferencePaths.shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths(
                createTreeRepository());
    }

    @Test
    public void shouldSearchAndFindPreferenceOfNonStandardConnectedFragment() {
        PreferenceSearcherTestCaseTwoNonStandardConnectedFragments.shouldSearchAndFindPreferenceOfNonStandardConnectedFragment(
                createTreeRepository());
    }

    @Test
    public void shouldSearchAndFindPreferenceOfNonStandardPreferenceFragment() {
        PreferenceSearcherTestCaseNonStandardPreferenceFragment.shouldSearchAndFindPreferenceOfNonStandardPreferenceFragment(
                createTreeRepository());
    }

    static void testSearch(final Fragment preferenceFragment,
                           final PreferenceSearchablePredicate preferenceSearchablePredicate,
                           final SearchResultsFilter searchResultsFilter,
                           final String keyword,
                           final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                           final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                           final PrincipalAndProxyProvider principalAndProxyProvider,
                           final Consumer<Set<PreferenceMatch>> checkPreferenceMatches,
                           final SearchablePreferenceScreenTreeRepository<Configuration> treeRepository,
                           final TreeBuilderListener<PreferenceScreenWithHost, Preference> treeBuilderListener) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(
                    fragmentActivity -> {
                        // Given
                        final Locale locale = Locale.GERMAN;
                        final MergedPreferenceScreen<Configuration> mergedPreferenceScreen =
                                getMergedPreferenceScreen(
                                        preferenceFragment,
                                        preferenceSearchablePredicate,
                                        fragmentActivity,
                                        createFragmentFactoryReturning(preferenceFragment),
                                        preferenceFragmentConnected2PreferenceProvider,
                                        preferenceDialogAndSearchableInfoProvider,
                                        principalAndProxyProvider,
                                        treeRepository,
                                        treeBuilderListener,
                                        locale,
                                        new DefaultPreferenceFragmentIdProvider());
                        final PreferenceSearcher<Configuration> preferenceSearcher =
                                new PreferenceSearcher<>(
                                        mergedPreferenceScreen.treeRepository(),
                                        searchResultsFilter,
                                        new PreferenceMatcher(new CaseInsensitiveSubstringMatcher()),
                                        fragmentActivity);

                        // When
                        final Set<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword, locale);

                        // Then
                        checkPreferenceMatches.accept(preferenceMatches);
                    });
        }
    }

    public static FragmentFactory createFragmentFactoryReturning(final Fragment preferenceFragment) {
        final FragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return new FragmentFactory() {

            @Override
            public <T extends Fragment> T instantiate(final Class<T> fragmentClass,
                                                      final Optional<PreferenceWithHost> src,
                                                      final Context context,
                                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
                return preferenceFragment.getClass().equals(fragmentClass) ?
                        (T) preferenceFragment :
                        defaultFragmentFactory.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
            }
        };
    }

    private static MergedPreferenceScreen<Configuration> getMergedPreferenceScreen(
            final Fragment preferenceFragment,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final FragmentActivity fragmentActivity,
            final FragmentFactory fragmentFactory,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
            final PrincipalAndProxyProvider principalAndProxyProvider,
            final SearchablePreferenceScreenTreeRepository<Configuration> treeRepository,
            final TreeBuilderListener<PreferenceScreenWithHost, Preference> treeBuilderListener,
            final Locale locale,
            final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        fragmentFactory,
                        FragmentInitializerFactory.createFragmentInitializer(
                                fragmentActivity,
                                TestActivity.FRAGMENT_CONTAINER_VIEW,
                                preferenceSearchablePredicate));
        final InstantiateAndInitializeFragment instantiateAndInitializeFragment =
                new Fragments(
                        new FragmentFactoryAndInitializerRegistry(fragmentFactoryAndInitializer),
                        fragmentActivity);
        final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider =
                new PreferenceScreenWithHostProvider(
                        instantiateAndInitializeFragment,
                        principalAndProxyProvider);
        final SearchablePreferenceScreenTreeProvider searchablePreferenceScreenTreeProvider =
                new SearchablePreferenceScreenTreeProvider(
                        new TreeToPojoTreeTransformer(
                                new PreferenceScreenToSearchablePreferenceScreenConverter(
                                        new PreferenceToSearchablePreferenceConverter(
                                                new IconProvider(new ReflectionIconResourceIdProvider()),
                                                new SearchableInfoAndDialogInfoProvider(
                                                        new ReversedListPreferenceSearchableInfoProvider().orElse(new BuiltinSearchableInfoProvider()),
                                                        new SearchableDialogInfoOfProvider(
                                                                PreferenceDialogsFactory.createPreferenceDialogs(
                                                                        fragmentActivity,
                                                                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                                                                        preferenceSearchablePredicate),
                                                                preferenceDialogAndSearchableInfoProvider)))),
                                preferenceFragmentIdProvider),
                        PreferenceScreenTreeBuilderFactory.createPreferenceScreenTreeBuilder(
                                preferenceScreenWithHostProvider,
                                preferenceFragmentConnected2PreferenceProvider,
                                new RootPreferenceFragmentOfActivityProvider() {

                                    @Override
                                    public Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final Class<? extends Activity> activityClass) {
                                        if (SettingsActivity.class.equals(activityClass)) {
                                            return Optional.of(SettingsFragment.class);
                                        }
                                        if (SettingsActivity2.class.equals(activityClass)) {
                                            return Optional.of(SettingsActivity2.SettingsFragment2.class);
                                        }
                                        return Optional.empty();
                                    }
                                },
                                edge -> true,
                                treeBuilderListener,
                                fragmentActivity),
                        locale);
        treeRepository.persistOrReplace(
                new SearchablePreferenceScreenTree<>(
                        searchablePreferenceScreenTreeProvider.getSearchablePreferenceScreenTree(
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                preferenceFragment.getClass(),
                                                Optional.empty())
                                        .orElseThrow()),
                        locale,
                        PersistableBundleTestFactory.createSomePersistableBundle()));
        return MergedPreferenceScreenFactory.createMergedPreferenceScreen(
                fragment -> {
                },
                preferencePath -> true,
                new DefaultPreferencePathDisplayer(),
                treeRepository,
                fragmentFactoryAndInitializer,
                new SearchResultsFragmentUI() {

                    @Override
                    public int getRootViewId() {
                        return de.KnollFrank.lib.settingssearch.R.layout.searchresults_fragment;
                    }

                    @Override
                    public RecyclerView getSearchResultsView(View rootView) {
                        return rootView.requireViewById(de.KnollFrank.lib.settingssearch.R.id.searchResults);
                    }
                },
                new DefaultMarkupsFactory(fragmentActivity),
                new SearchResultsByPreferencePathSorter(),
                instantiateAndInitializeFragment,
                Map.of(),
                new PrincipalAndProxyProvider(ImmutableBiMap.of()),
                (activity, settingsFragment, setting2Highlight) -> {
                },
                fragmentActivity);
    }

    private static class PreferenceDialogAndSearchableInfoProvider implements de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider {

        @Override
        public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
            return preference instanceof CustomDialogPreference || KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER.equals(preference.getKey()) ?
                    Optional.of(
                            new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                    new CustomDialogFragment(),
                                    CustomDialogFragment::getSearchableInfo)) :
                    Optional.empty();
        }
    }

    private SearchablePreferenceScreenTreeRepository<Configuration> createTreeRepository() {
        return SearchablePreferenceScreenTreeRepository.of(
                preferencesRoomDatabase.searchablePreferenceScreenTreeDAO(),
                new ConfigurationBundleConverter());
    }
}