package de.KnollFrank.lib.settingssearch.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.SearchablePreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentInitializer;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.factory.FragmentFactoryAndInitializerWithCache;
import de.KnollFrank.lib.settingssearch.graph.DefaultSearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.MergedPreferenceScreenProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.SearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.BuiltinSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
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
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final CheckBoxPreference preference = new CheckBoxPreference(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format("Checkbox %s file", keyword));
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindTitleOfPreferenceCategory() {
        final String keyword = "This is a preference category";
        final String keyOfPreference = "keyOfPreferenceCategory";
        testSearch(
                PreferenceFragment.fromSinglePreference(
                        context -> {
                            final PreferenceCategory preference = new PreferenceCategory(context);
                            preference.setKey(keyOfPreference);
                            preference.setTitle(String.format(keyword));
                            return preference;
                        }),
                (preference, host) -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, host) -> !(preference instanceof CheckBoxPreference && keyOfPreference.equals(preference.getKey())),
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, host) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItem(keyOfPreference)));
    }

    @Test
    public void shouldSearchAndFindInTwoCustomDialogPreferences() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference1 = "keyOfCustomDialogPreference1";
        final String keyOfPreference2 = "keyOfCustomDialogPreference2";
        testSearch(
                new PreferenceFragment(
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
                (preference, host) -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
                                hasItems(keyOfPreference1, keyOfPreference2)));
    }

    @Test
    public void shouldSearchAndFindInPreferenceWithOnPreferenceClickListener() {
        final String keyword = "some text in a custom dialog";
        final String keyOfPreference = "keyOfPreferenceWithOnPreferenceClickListener";
        testSearch(
                new PrefsFragmentFirst(),
                (preference, host) -> true,
                keyword,
                (preference, hostOfPreference) -> Optional.empty(),
                new PreferenceDialogAndSearchableInfoProvider(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
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
                (preference, hostOfPreference) -> Optional.empty(),
                (preference, hostOfPreference) -> Optional.empty(),
                preferenceMatches ->
                        assertThat(
                                getKeys(preferenceMatches),
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
                           final IsPreferenceSearchable isPreferenceSearchable,
                           final String keyword,
                           final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                           final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                           final Consumer<List<PreferenceMatch>> checkPreferenceMatches) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final MergedPreferenceScreen mergedPreferenceScreen =
                        getMergedPreferenceScreen(
                                preferenceFragment,
                                isPreferenceSearchable,
                                fragmentActivity,
                                createFragmentFactoryReturning(preferenceFragment),
                                preferenceConnected2PreferenceFragmentProvider,
                                preferenceDialogAndSearchableInfoProvider);
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(
                                mergedPreferenceScreen,
                                new SearchableInfoAttribute());

                // When
                final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(keyword);

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
            final IsPreferenceSearchable isPreferenceSearchable,
            final FragmentActivity fragmentActivity,
            final FragmentFactory fragmentFactory,
            final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
            final de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        final DefaultFragmentInitializer fragmentInitializer =
                new DefaultFragmentInitializer(
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        final FragmentFactoryAndInitializer fragmentFactoryAndInitializer =
                new FragmentFactoryAndInitializer(
                        fragmentFactory,
                        fragmentInitializer);
        final Fragments fragments =
                new Fragments(
                        new FragmentFactoryAndInitializerWithCache(fragmentFactoryAndInitializer),
                        fragmentActivity);
        final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider =
                new DefaultSearchablePreferenceScreenGraphProvider(
                        preferenceFragment.getClass().getName(),
                        new PreferenceScreenWithHostProvider(
                                fragments,
                                new SearchablePreferenceScreenProvider(
                                        new IsPreferenceVisibleAndSearchable(
                                                isPreferenceSearchable))),
                        preferenceConnected2PreferenceFragmentProvider,
                        preferenceScreenGraph -> {
                        },
                        new SearchableInfoAndDialogInfoProvider(
                                new ReversedListPreferenceSearchableInfoProvider().orElse(new BuiltinSearchableInfoProvider()),
                                new SearchableDialogInfoOfProvider(
                                        fragmentInitializer,
                                        preferenceDialogAndSearchableInfoProvider)));
        final MergedPreferenceScreenProvider mergedPreferenceScreenProvider =
                new MergedPreferenceScreenProvider(
                        new SearchableInfoAttribute(),
                        false,
                        fragmentFactoryAndInitializer,
                        PreferenceManagerProvider.getPreferenceManager(
                                fragments,
                                preferenceFragment.getClass()));

        return mergedPreferenceScreenProvider.getMergedPreferenceScreen(
                searchablePreferenceScreenGraphProvider.getSearchablePreferenceScreenGraph());
    }

    static List<String> getKeys(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .map(SearchablePreferencePOJO::key)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static class PreferenceDialogAndSearchableInfoProvider implements de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider {

        @Override
        public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
            return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                    Optional.of(
                            new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                    new CustomDialogFragment(),
                                    CustomDialogFragment::getSearchableInfo)) :
                    Optional.empty();
        }
    }
}