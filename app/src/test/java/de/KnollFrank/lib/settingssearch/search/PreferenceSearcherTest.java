package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static de.KnollFrank.lib.settingssearch.search.PreferenceMatchHelper.getKeySet;

import android.content.Context;
import android.view.View;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.results.DefaultMarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsByPreferencePathSorter;
import de.KnollFrank.lib.settingssearch.results.recyclerview.DefaultPreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class PreferenceSearcherTest {

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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindPreference_includePreferenceInSearchResults() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        final PreferenceFragmentCompat preferenceFragment =
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        });
        final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate =
                new IncludePreferenceInSearchResultsPredicate() {

                    @Override
                    public boolean includePreferenceInSearchResults(final SearchablePreference preference) {
                        return Optional.of(keyOfPreference).equals(preference.getKey()) && preferenceFragment.getClass().equals(preference.getHost());
                    }
                };
        testSearch(
                preferenceFragment,
                (preference, hostOfPreference) -> true,
                includePreferenceInSearchResultsPredicate,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindPreference_excludePreferenceFromSearchResults() {
        final String keyword = "fourth";
        final String keyOfPreference = "fourthfile";
        final PreferenceFragmentCompat preferenceFragment =
                PreferenceFragmentFactory.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        });
        final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate =
                new IncludePreferenceInSearchResultsPredicate() {

                    @Override
                    public boolean includePreferenceInSearchResults(final SearchablePreference preference) {
                        return !(Optional.of(keyOfPreference).equals(preference.getKey()) && preferenceFragment.getClass().equals(preference.getHost()));
                    }
                };
        testSearch(
                preferenceFragment,
                (preference, hostOfPreference) -> true,
                includePreferenceInSearchResultsPredicate,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreference))));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfNestedPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreference))));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItems(keyOfPreference1, keyOfPreference2)));
    }

    @Test
    public void shouldSearchAndFindInPreferenceWithOnPreferenceClickListener() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = "keyOfPreferenceWithOnPreferenceClickListener";
        testSearch(
                new PrefsFragmentFirst(),
                (preference, hostOfPreference) -> true,
                preference -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                hasItem(keyOfPreference)));
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
                preferenceMatches ->
                        assertThat(
                                getKeySet(preferenceMatches),
                                not(hasItem(keyOfPreference))));
    }

    @Test
    public void shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths() {
        PreferenceSearcherTestCaseTwoDifferentPreferencePaths.shouldSearchAndFindPreferenceWithTwoDifferentPreferencePaths();
    }

    @Test
    public void shouldSearchAndFindPreferenceOfNonStandardConnectedFragment() {
        PreferenceSearcherTestCaseTwoNonStandardConnectedFragments.shouldSearchAndFindPreferenceOfNonStandardConnectedFragment();
    }

    static void testSearch(final PreferenceFragmentCompat preferenceFragment,
                           final PreferenceSearchablePredicate preferenceSearchablePredicate,
                           final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
                           final String keyword,
                           final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                           final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                           final Consumer<Set<PreferenceMatch>> checkPreferenceMatches) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final MergedPreferenceScreen mergedPreferenceScreen =
                        getMergedPreferenceScreen(
                                preferenceFragment,
                                preferenceSearchablePredicate,
                                fragmentActivity,
                                createFragmentFactoryReturning(preferenceFragment),
                                preferenceFragmentConnected2PreferenceProvider,
                                preferenceDialogAndSearchableInfoProvider);
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                mergedPreferenceScreen.preferences(),
                                includePreferenceInSearchResultsPredicate);

                // When
                final Set<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

                // Then
                checkPreferenceMatches.accept(preferenceMatches);
            });
        }
    }

    private static FragmentFactory createFragmentFactoryReturning(final PreferenceFragmentCompat preferenceFragment) {
        final DefaultFragmentFactory defaultFragmentFactory = new DefaultFragmentFactory();
        return (fragmentClassName, src, context) ->
                preferenceFragment.getClass().getName().equals(fragmentClassName) ?
                        preferenceFragment :
                        defaultFragmentFactory.instantiate(fragmentClassName, src, context);
    }

    private static MergedPreferenceScreen getMergedPreferenceScreen(
            final PreferenceFragmentCompat preferenceFragment,
            final PreferenceSearchablePredicate preferenceSearchablePredicate,
            final FragmentActivity fragmentActivity,
            final FragmentFactory fragmentFactory,
            final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
            final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        final DefaultFragmentInitializer fragmentInitializer =
                new DefaultFragmentInitializer(
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW,
                        OnUiThreadRunnerFactory.fromActivity(fragmentActivity));
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        fragmentFactory,
                        fragmentInitializer);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        fragmentActivity);
        final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                new SearchablePreferenceScreenGraphProvider(
                        preferenceFragment.getClass().getName(),
                        new PreferenceScreenWithHostProvider(
                                fragments,
                                new SearchablePreferenceScreenProvider(
                                        new PreferenceVisibleAndSearchablePredicate(
                                                preferenceSearchablePredicate))),
                        preferenceFragmentConnected2PreferenceProvider,
                        preferenceScreenGraph -> {
                        },
                        preferenceScreenWithHost -> {
                        },
                        new Preference2SearchablePreferenceConverter(
                                new IconProvider(new ReflectionIconResourceIdProvider()),
                                new SearchableInfoAndDialogInfoProvider(
                                        new ReversedListPreferenceSearchableInfoProvider().orElse(new BuiltinSearchableInfoProvider()),
                                        new SearchableDialogInfoOfProvider(
                                                fragmentInitializer,
                                                preferenceDialogAndSearchableInfoProvider)),
                                new IdGenerator()));
        return MergedPreferenceScreenFactory.createMergedPreferenceScreen(
                TestActivity.FRAGMENT_CONTAINER_VIEW,
                _preferenceFragment -> {
                },
                preferencePath -> true,
                new DefaultPreferencePathDisplayer(),
                fragmentActivity.getSupportFragmentManager(),
                MergedPreferenceScreenDataFactory.getPreferences(
                        searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph()),
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
                fragmentActivity,
                searchResults -> searchResults,
                new SearchResultsByPreferencePathSorter());
    }

    private static class PreferenceDialogAndSearchableInfoProvider implements de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider {

        @Override
        public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
            return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                    Optional.of(
                            new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                    new CustomDialogFragment(),
                                    CustomDialogFragment::getSearchableInfo)) :
                    Optional.empty();
        }
    }
}